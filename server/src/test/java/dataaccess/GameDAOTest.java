package dataaccess;

import dataaccess.game.DatabaseGameDAO;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {
    DatabaseGameDAO gameDAO;
    int id = 0;
    GameData game = new GameData(id, "game");

    {
        try {
            gameDAO = new DatabaseGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void init() {
        try {
            gameDAO.clearGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertGameSuccess() {
        try {
            gameDAO.insertGame(game);
            var result = gameDAO.selectGame(id);

            assertEquals(game, result);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertGameFail() {
    }

    @Test
    void updateGameSuccess() {
    }

    @Test
    void updateGameFail() {
    }

    @Test
    void selectGameSuccess() {
    }

    @Test
    void selectGameFail() {
    }

    @Test
    void selectAllGamesSuccess() {
    }

    @Test
    void selectAllGamesFail() {
    }

    @Test
    void clearGames() {
    }
}