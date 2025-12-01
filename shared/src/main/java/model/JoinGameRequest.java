package model;

import chess.ChessGame;

public record JoinGameRequest(ChessGame.TeamColor playerColor, int gameID) {
    public JoinGameRequest(int gameID) {
        this(null, gameID);
    }
}
