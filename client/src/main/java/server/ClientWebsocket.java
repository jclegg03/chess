package server;

import com.google.gson.Gson;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import serializer.Serializer;

public class ClientWebsocket extends Endpoint {
    private Session session;
    private NotificationHandler notificationHandler;
    private Gson serializer = Serializer.serializer();

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
                    ServerMessage notification = serializer.fromJson(message, ServerMessage.class);

                    if(notification.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
                        notificationHandler.updateGame(notification.getGame());
                        return;
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

    public void sendMessage(UserGameCommand message) {
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(message));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
