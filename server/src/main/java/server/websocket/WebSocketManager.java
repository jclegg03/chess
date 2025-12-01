package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;

public class WebSocketManager implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
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
