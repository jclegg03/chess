package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import io.javalin.http.HttpStatus;
import io.javalin.websocket.*;
import server.ServerException;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashMap;

public class WebSocketManager implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private Service service;
    private final HashMap<Integer, GameRoom> rooms;

    public WebSocketManager(Service service) {
        this.service = service;
        this.rooms = new HashMap<>();
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket Closed.");
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket Opened.");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, ctx);
            case MAKE_MOVE -> makeMove(command, ctx);
            case LEAVE -> leave(command, ctx);
            case RESIGN -> resign(command, ctx);
        }
    }

    private void makeMove(UserGameCommand command, WsMessageContext ctx) {
        try {
            var room = getRoom(command.getGameID());
            var auth = service.getAuth(command.getAuthToken());

            var game = service.makeMove(auth, command.getGameID(), command.getMove());
            var updateMessage = new ServerMessage(game.game());
            room.tellEveryone(updateMessage, null);

            String text = command.getMove().toString();
            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, text);
            room.tellEveryone(serverMessage, ctx.session);
        } catch (Exception e) {
            handleError(e, ctx);
        }
    }

    private void connect(UserGameCommand command, WsMessageContext ctx) {
        try {
            var room = getRoom(command.getGameID());
            room.addParticipant(ctx.session);
            var auth = service.getAuth(command.getAuthToken());

            var game = service.getGame(auth, command.getGameID());
            if(game == null) {
                handleError(new ServerException("Bad game ID provided", HttpStatus.BAD_REQUEST), ctx);
            }

            String text = auth.username() + " joined" + getConnectText(command.getJoinType());
            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, text);
            room.tellEveryone(serverMessage, ctx.session);


            var gameMessage = new ServerMessage(game.game());
            ctx.session.getRemote().sendString(new Gson().toJson(gameMessage));
        } catch (Exception e) {
            handleError(e, ctx);
        }
    }

    private String getConnectText(UserGameCommand.JoinType type) {
        if(type == UserGameCommand.JoinType.OBSERVER) {
            return " as an observer.";
        }

        String color = type == UserGameCommand.JoinType.WHITE ? "white" : "black";
        return " as the " + color + " player.";
    }

    private void resign(UserGameCommand command, WsMessageContext ctx) {
        try {
            var room = getRoom(command.getGameID());
            var auth = service.getAuth(command.getAuthToken());
            String text = auth.username() + " has resigned.";

            service.resign(auth, command.getGameID());

            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, text);
            room.tellEveryone(serverMessage, null);

        } catch (Exception e) {
            handleError(e, ctx);
        }
    }

    private void leave(UserGameCommand command, WsMessageContext ctx) {
        try {
            var room = getRoom(command.getGameID());
            room.removeParticipant(ctx.session);
            var auth = service.getAuth(command.getAuthToken());
            String text = auth.username() + " has left the game.";

            service.leaveGame(auth, command.getGameID());

            var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, text);
            room.tellEveryone(serverMessage, ctx.session);
        } catch (Exception e) {
            handleError(e, ctx);
        }
    }

    private GameRoom getRoom(int id) {
        if(id == -1) {
            throw new RuntimeException("Bad game id");
        }
        return rooms.computeIfAbsent(id, k -> new GameRoom());
    }

    private void handleError(Exception e, WsMessageContext ctx) {
        try {
            ctx.session.getRemote().sendString(
                    new Gson().toJson(new ServerMessage(e.getMessage())));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
