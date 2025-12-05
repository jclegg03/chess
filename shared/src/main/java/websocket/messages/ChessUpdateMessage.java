package websocket.messages;

import chess.ChessGame;

public class ChessUpdateMessage extends ServerMessage {
    private ChessGame game;

    public ChessUpdateMessage(String text, ChessGame game) {
        super(ServerMessageType.LOAD_GAME, text);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
