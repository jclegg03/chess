package server;

import io.javalin.websocket.*;

public class WebSocketManager implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    @Override
    public void handleClose(WsCloseContext wsCloseContext) throws Exception {

    }

    @Override
    public void handleConnect(WsConnectContext wsConnectContext) throws Exception {

    }

    @Override
    public void handleMessage(WsMessageContext wsMessageContext) throws Exception {

    }
}
