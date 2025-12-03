package server;

import chess.ChessGame;

public record JoinGameResponse(boolean joinedGame, ChessGame game, String message) {
}
