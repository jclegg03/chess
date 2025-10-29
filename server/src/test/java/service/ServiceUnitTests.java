package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.auth.LocalAuthDAO;
import dataaccess.game.LocalGameDAO;
import dataaccess.user.DatabaseUserDAO;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import model.UserData;
import server.ServerException;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceUnitTests {
    final UserData sampleUser = new UserData("username", "password", "email@mail.com");
    static AuthData sampleAuth = new AuthData("", "");
    final Service service;
    DatabaseUserDAO userDAO;

    {
        try {
            userDAO = new DatabaseUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        service = new Service(userDAO, new LocalGameDAO(), new LocalAuthDAO());
    }


    public void setup() {
        try {
            sampleAuth = service.createUser(sampleUser);
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void clearDB() {
        try {
            userDAO.clearUsers();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateUserSuccess() {
        try {
            AuthData auth = service.createUser(sampleUser);
            sampleAuth = new AuthData("username", auth.authToken());
            assertEquals(sampleAuth, auth);
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateUserFails() {
        setup();
        assertThrows(ServerException.class, () -> service.createUser(sampleUser));
        assertThrows(ServerException.class, () -> service.createUser(new UserData(null, "", "")));
        assertThrows(ServerException.class, () -> service.createUser(new UserData("", null, "")));
        assertThrows(ServerException.class, () -> service.createUser(new UserData("", "", null)));
    }

    @Test
    public void testGetAuthSuccess() {
        try {
            setup();
            assertEquals(sampleAuth, service.getAuth(sampleAuth.authToken()));
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testNullAuthCausesFail() {
        assertThrows(ServerException.class, () -> service.joinGame(null, ChessGame.TeamColor.WHITE, 0));
    }

    //Again impossible to test without implementing createUser
    @Test
    public void testLoginSuccess() {
        try {
            setup();
            AuthData authFromLogin = service.login(sampleUser);

            assertEquals(sampleAuth.username(), authFromLogin.username());
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLoginSuccessNoEmail() {
        try {
            setup();
            service.login(new UserData(sampleUser.username(), sampleUser.password(), null));
        }
        catch (ServerException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testLoginFails() {
        assertThrows(ServerException.class, () -> service.login(sampleUser));
        setup();
        assertThrows(ServerException.class, () -> service.login(new UserData(null, "", "")));
        assertThrows(ServerException.class, () -> service.login(new UserData("", null, "")));
        assertThrows(ServerException.class, () -> service.login(null));
        assertThrows(ServerException.class, () -> service.login(new UserData(sampleUser.username(), "", "")));
    }

    //Best to test this one after writing functions that check auth status.
    @Test
    public void testLogoutWorks() {
        try {
            //try doing something with an auth that has been logged out
            setup();
            service.logout(sampleAuth);
            assertThrows(ServerException.class, () -> service.createGame(sampleAuth, "Cool Game"));
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testLogoutFails() {
        assertThrows(ServerException.class, () -> service.logout(sampleAuth));

        assertThrows(ServerException.class, () -> service.logout(null));
    }

    @Test
    public void testCreateAndListGamesWorks() {
        try {
            setup();

            HashSet<GameData> games = new HashSet<>();

            var gameID = service.createGame(sampleAuth, "game1");
            games.add(new GameData(gameID, "game1"));

            HashSet<GameData> gamesInMem = new HashSet<>(List.of(service.listGames(sampleAuth)));

            assertEquals(gamesInMem, games);


            gameID = service.createGame(sampleAuth, "game2");
            games.add(new GameData(gameID, "game2"));
            gamesInMem = new HashSet<>(List.of(service.listGames(sampleAuth)));

            assertEquals(gamesInMem, games);
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateAndListGamesFails() {
        assertThrows(ServerException.class, () -> service.createGame(sampleAuth, ""));
        assertThrows(ServerException.class, () -> service.listGames(sampleAuth));

        setup();
        assertThrows(ServerException.class, () -> service.createGame(sampleAuth, null));
    }

    @Test
    public void testJoinGameBadColor() {
        try {
            setup();
            var gameID = service.createGame(sampleAuth, "Game");

            assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, null, gameID));
        }
        catch (ServerException e) {
            throw new RuntimeException();
        }
    }

    @Test
    public void testJoinGameGeneralFails() {
        assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, 0));

        setup();
        int gameID = 0;
        // test joining a nonexistent game
        assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID));
    }

    @Test
    public void testJoinGameWhite() {
        try {
            setup();
            var gameID = service.createGame(sampleAuth, "Game");

            service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, gameID);
            var game = service.listGames(sampleAuth)[0];

            var testGame = new GameData(gameID, "Game");
            testGame = testGame.setWhiteUsername(sampleAuth.username());

            assertEquals(testGame, game);
            assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, gameID));
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testJoinGameBlack() {
        try {
            setup();
            var gameID = service.createGame(sampleAuth, "Game");
            service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID);
            var game = service.listGames(sampleAuth)[0];

            var testGame = new GameData(gameID, "Game");
            testGame = testGame.setBlackUsername(sampleAuth.username());
            assertEquals(testGame, game);

            assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID));
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testClearData() {
        try {
            setup();
            service.createGame(sampleAuth, "");
            service.clearData();

            assertThrows(ServerException.class, () -> service.login(sampleUser));
            assertThrows(ServerException.class, () -> service.listGames(sampleAuth));
        }
        catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }
}
