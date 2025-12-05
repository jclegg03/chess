package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
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

    }

    private void connect(UserGameCommand command, WsMessageContext ctx) {
        try {
            var room = getRoom(command.getGameID());
            room.addParticipant(ctx.session);
            var auth = service.getAuth(command.getAuthToken());
            var msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, auth.username());
            room.tellEveryone(msg, null);
        } catch (Exception e) {
            handleError(e, ctx);
        }
    }

    private void resign(UserGameCommand command, WsMessageContext ctx) {

    }

    private void leave(UserGameCommand command, WsMessageContext ctx) {

    }

    private GameRoom getRoom(int id) {
        return rooms.computeIfAbsent(id, k -> new GameRoom());
    }

    private void handleError(Exception e, WsMessageContext ctx) {
        try {
            ctx.session.getRemote().sendString(
                    new Gson().toJson(
                            new ServerMessage(
                                    ServerMessage.ServerMessageType.ERROR, e.getMessage()
                            )));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
