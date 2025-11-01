package model;

import chess.ChessGame;

import java.util.Objects;

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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameData gameData = (GameData) o;
        return gameID == gameData.gameID &&
                Objects.equals(game, gameData.game) &&
                Objects.equals(gameName, gameData.gameName) &&
                Objects.equals(whiteUsername, gameData.whiteUsername) &&
                Objects.equals(blackUsername, gameData.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }
}
