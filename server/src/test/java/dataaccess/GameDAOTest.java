package dataaccess;

import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
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
        try {
            gameDAO.insertGame(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DataAccessException.class, () -> gameDAO.insertGame(game));
    }

    @Test
    void updateGameSuccess() {
        try {
            gameDAO.insertGame(game);
            game.game().makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(4, 1)));
            gameDAO.updateGame(game.gameID(), game);
            assertEquals(game, gameDAO.selectGame(game.gameID()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (InvalidMoveException e) {
            throw new RuntimeException("You idiot. Make a good move for the test.");
        }
    }

    @Test
    void updateGameFail() {
    }

    @Test
    void selectGameSuccess() {
        insertGameSuccess();
    }

    @Test
    void selectGameFail() {
        try {
            assertNull(gameDAO.selectGame(0));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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