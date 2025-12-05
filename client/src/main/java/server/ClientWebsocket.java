package server;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.messages.ChessUpdateMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ClientWebsocket extends Endpoint {
    private Session session;
    private NotificationHandler notificationHandler;

    public ClientWebsocket(String url, NotificationHandler handler) {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = handler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);

                    if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        var chessUpdate = getChessMessage(message);
                        notificationHandler.updateGame(chessUpdate.getGame());
                    }

                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private ChessUpdateMessage getChessMessage(String message) {
        return new Gson().fromJson(message, ChessUpdateMessage.class);
    }
}
