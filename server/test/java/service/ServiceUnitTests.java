package service;

import chess.ChessGame;
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
    final Service service = new Service();


    public void setup() {
        sampleAuth = service.createUser(sampleUser);
    }

    @Test
    public void testCreateUser() {
        AuthData auth = service.createUser(sampleUser);
        sampleAuth = new AuthData("username", auth.authToken());
        assertEquals(sampleAuth, auth);

        assertThrows(ServerException.class, () -> service.createUser(sampleUser));
    }

    // Unfortunately it is impossible to test getUser without first implementing createUser.
    @Test
    public void testGetUser() {
        assertNull(service.getUser("username"));

        service.createUser(sampleUser);
        assertEquals(sampleUser, service.getUser(sampleUser.username()));
    }

    //Again impossible to test without implementing createUser
    @Test
    public void testLogin() {
        assertThrows(ServerException.class, () -> service.login(sampleUser));

        setup();
        AuthData authFromLogin = service.login(sampleUser);

        assertEquals(sampleAuth.username(), authFromLogin.username());
    }

    //Best to test this one after writing functions that check auth status.
    @Test
    public void testLogout() {
        //try doing something with an auth that has been logged out
        setup();
        service.logout(sampleAuth);
        //attempt to log out with no one logged in. This should just throw a server error.
        assertThrows(ServerException.class, () -> service.logout(sampleAuth));

        assertThrows(Exception.class, () -> service.createGame(sampleAuth, "Cool Game"));
    }

    @Test
    public void testCreateAndListGames() {
        assertThrows(ServerException.class, () -> service.createGame(sampleAuth, ""));
        assertThrows(ServerException.class, () -> service.listGames(sampleAuth));

        setup();

        HashSet<GameData> games = new HashSet<>();
        games.add(new GameData(service.getCurrentGameID(), "game1"));

        service.createGame(sampleAuth, "game1");

        HashSet<GameData> gamesInMem = new HashSet<>(List.of(service.listGames(sampleAuth)));

        assertEquals(gamesInMem, games);

        games.add(new GameData(service.getCurrentGameID(), "game2"));
        service.createGame(sampleAuth, "game2");
        gamesInMem = new HashSet<>(List.of(service.listGames(sampleAuth)));

        assertEquals(gamesInMem, games);
    }

    @Test
    public void testJoinGameGeneral() {
        assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, 0));

        setup();
        int gameID = service.getCurrentGameID();
        // test joining a nonexistent game
        assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID));
    }

    @Test
    public void testJoinGameWhite() {
        setup();
        int gameID = service.getCurrentGameID();
        service.createGame(sampleAuth, "Game");

        service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, gameID);
        var game = service.listGames(sampleAuth)[0];

        var testGame = new GameData(gameID, "Game");
        testGame = testGame.setWhiteUsername(sampleAuth.username());

        assertEquals(testGame, game);
        assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, gameID));
    }

    @Test
    public void testJoinGameBlack() {
        setup();
        int gameID = service.getCurrentGameID();
        service.createGame(sampleAuth, "Game");
        service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID);
        var game = service.listGames(sampleAuth)[0];

        var testGame = new GameData(gameID, "Game");
        testGame = testGame.setBlackUsername(sampleAuth.username());

        assertEquals(testGame, game);;
        assertThrows(ServerException.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID));
    }

    @Test
    public void testClearData() {
        setup();
        service.createGame(sampleAuth, "");
        service.clearData();

        assertNull(service.getUser(sampleUser.username()));
        assertThrows(ServerException.class, () -> service.listGames(sampleAuth));
    }
}
