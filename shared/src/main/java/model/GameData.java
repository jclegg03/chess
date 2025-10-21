package model;

import chess.ChessGame;

public record GameData (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    /**
     * Calls the normal constructor with the given gameID.
     * username parameters are empty strings, not null.
     * creates a new ChessGame.
     * @param gameID the ID for the game
     */
    public GameData(int gameID, String gameName) {
        this(gameID, null, null, gameName, new ChessGame());
    }

    public GameData setWhiteUsername(String whiteUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    public GameData setBlackUsername(String blackUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
