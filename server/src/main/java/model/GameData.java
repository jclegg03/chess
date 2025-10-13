package model;

import chess.ChessGame;

import java.util.Objects;

public record GameData (int gameID, String whiteUsername, String blackUsername, ChessGame game) {
    public GameData(int gameID) {
        this(gameID, "", "", new ChessGame());
    }

    public GameData setWhiteUsername(String whiteUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, game);
    }

    public GameData setBlackUsername(String blackUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, game);
    }
}
