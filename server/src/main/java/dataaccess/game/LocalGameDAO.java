package dataaccess.game;

import dataaccess.DataAccessException;
import io.javalin.http.HttpStatus;
import model.GameData;

import java.util.HashSet;

public class LocalGameDAO implements GameDAO {
    private HashSet<GameData> games;
    private static int currentID = 0;

    public LocalGameDAO() {
        this.games = new HashSet<>();
    }

    @Override
    public int insertGame(GameData game) throws DataAccessException {
        if(game.gameName() == null) {
            throw new DataAccessException("Game name cannot be null.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        games.add(new GameData(++currentID, game.gameName()));
        return currentID;
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        if(games.remove(selectGame(gameID))) {
            games.add(game);
        }
    }

    @Override
    public GameData selectGame(int gameID) throws DataAccessException {
        for(GameData game : games) {
            if(game.gameID() == gameID) {
                return game;
            }
        }

        return null;
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
