package model;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.Objects;

public record GameData (int gameID, String whiteUsername, String blackUsername,
                        String gameName, ChessGame game, ArrayList<String> observers) {
    /**
     * Calls the normal constructor with the given gameID.
     * username parameters are empty strings, not null.
     * creates a new ChessGame.
     * @param gameID the ID for the game
     */
    public GameData(int gameID, String gameName) {
        this(gameID, null, null, gameName, new ChessGame(), new ArrayList<>());
    }

    public GameData setWhiteUsername(String whiteUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game, observers);
    }

    public GameData setBlackUsername(String blackUsername) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game, observers);
    }

    public void addObserver(String username) {
        observers.addLast(username);
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
