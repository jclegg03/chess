package server;

import chess.ChessGame;

public record JoinGameResponse(boolean joinedGame, ChessGame game, String message, int id) {
    public JoinGameResponse(boolean joinedGame, ChessGame game, String message) {
        this(joinedGame, game, message, -1);
    }
}
