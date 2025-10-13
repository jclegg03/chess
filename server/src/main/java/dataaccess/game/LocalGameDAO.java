package dataaccess.game;

import dataaccess.DataAccessException;
import io.javalin.http.HttpStatus;
import model.GameData;

import java.util.HashSet;

public class LocalGameDAO implements GameDAO {
    private HashSet<GameData> games;

    public LocalGameDAO() {
        this.games = new HashSet<>();
    }

    @Override
    public void insertGame(GameData game) throws DataAccessException {
        games.add(game);
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        games.remove(selectGame(gameID));
        games.add(game);
    }

    @Override
    public GameData selectGame(int gameID) throws DataAccessException {
        for(GameData game : games) {
            if(game.getGameID() == gameID) {
                return game;
            }
        }

        throw new DataAccessException("Error: no game with gameID " + gameID, HttpStatus.BAD_REQUEST);
    }

    @Override
    public GameData[] selectAllGames() throws DataAccessException {
        GameData[] gameList = new GameData[games.size()];
        int i = 0;
        for(GameData game : games) {
            gameList[i++] = game;
        }

        return gameList;
    }

    @Override
    public void clearGames() throws DataAccessException {
        games = new HashSet<>();
    }
}
