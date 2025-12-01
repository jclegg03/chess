package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import service.GameRooms;
import websocket.commands.UserGameCommand;

public class WebSocketManager implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private GameRooms gameRooms;


    public WebSocketManager(GameRooms gameRooms) {
        this.gameRooms = gameRooms;
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

    }
}
