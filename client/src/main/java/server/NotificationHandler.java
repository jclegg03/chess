package server;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public interface NotificationHandler {
    public void notify(ServerMessage message);
    public void updateGame(ChessGame game);
}
