package passoff.service;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import model.UserData;
import server.ServerException;
import service.Service;

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
    public void testCreateUserSuccess() {
        AuthData auth = service.createUser(sampleUser);
        sampleAuth = new AuthData("username", auth.authToken());
        assertEquals(sampleAuth, auth);
    }

    @Test
    public void testCreateUserTwice() {
        setup();
        assertThrows(ServerException.class, () -> service.createUser(sampleUser));
    }

    //Again impossible to test without implementing createUser
    @Test
    public void testLoginSuccess() {
        setup();
        AuthData authFromLogin = service.login(sampleUser);

        assertEquals(sampleAuth.username(), authFromLogin.username());
    }

    @Test
    public void testLoginFails() {
        assertThrows(ServerException.class, () -> service.login(sampleUser));
    }

    //Best to test this one after writing functions that check auth status.
    @Test
    public void testLogoutWorks() {
        //try doing something with an auth that has been logged out
        setup();
        service.logout(sampleAuth);
        assertThrows(ServerException.class, () -> service.createGame(sampleAuth, "Cool Game"));
    }

    @Test
    public void testLogoutFails() {
        assertThrows(ServerException.class, () -> service.logout(sampleAuth));
    }

    @Test
    public void testCreateAndListGamesWorks() {
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

    @Test
    public void testCreateAndListGamesFails() {
        assertThrows(ServerException.class, () -> service.createGame(sampleAuth, ""));
        assertThrows(ServerException.class, () -> service.listGames(sampleAuth));
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
        setup();
        var gameID = service.createGame(sampleAuth, "Game");

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
        var gameID = service.createGame(sampleAuth, "Game");
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

        assertThrows(ServerException.class, () -> service.login(sampleUser));
        assertThrows(ServerException.class, () -> service.listGames(sampleAuth));
    }
}
