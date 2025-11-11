package dataaccess.game;

import dataaccess.DataAccessException;
import model.GameData;

public interface GameDAO {
    public int insertGame(GameData game) throws DataAccessException;
    public void updateGame(int gameID, GameData game) throws DataAccessException;
    public GameData selectGame(int gameID) throws DataAccessException;
    public GameData[] selectAllGames() throws DataAccessException;
    public void clearGames() throws DataAccessException;
}
