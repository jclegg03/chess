package dataaccess.game;

import dataaccess.DataAccessException;
import model.GameData;

public class DatabaseGameDAO implements GameDAO {
    @Override
    public void insertGame(GameData game) throws DataAccessException {

    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {

    }

    @Override
    public GameData selectGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData[] selectAllGames() throws DataAccessException {
        return new GameData[0];
    }

    @Override
    public void clearGames() throws DataAccessException {

    }
}
