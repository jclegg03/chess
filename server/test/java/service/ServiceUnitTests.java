package service;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;
import model.UserData;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceUnitTests {
    UserData sampleUser = new UserData("username", "password", "email@mail.com");
    AuthData sampleAuth = new AuthData("username", "" + Objects.hash("username", "password"));
    Service service = new Service();

    @Test
    public void testCreateUser() {
        AuthData auth = service.createUser(sampleUser);
        assert sampleAuth.equals(auth);
    }

    // Unfortunately it is impossible to test getUser without first implementing createUser.
    @Test
    public void testGetUser() {
        assert service.getUser("username") == null;

        service.createUser(sampleUser);
        assert sampleUser.equals(service.getUser(sampleUser.username()));
    }

    //Again impossible to test without implementing createUser
    @Test
    public void testLogin() {
        assert service.login(sampleUser) == null;

        AuthData authFromCreate = service.createUser(sampleUser);
        AuthData authFromLogin = service.login(sampleUser);

        assert sampleAuth.equals(authFromCreate) && sampleAuth.equals(authFromLogin);
    }

    //Best to test this one after writing functions that check auth status.
    @Test
    public void testLogout() {
        //attempt to log out with no one logged in. This should just do nothing.
        service.logout(sampleAuth);

        //try doing something with an auth that has been logged out
        var expiredAuth = service.login(sampleUser);
        service.logout(expiredAuth);

        assertThrows(Exception.class, () -> service.createGame(expiredAuth, "Cool Game"));
    }

    @Test
    public void testCreateAndListGames() {
        service.createUser(sampleUser);

        HashSet<GameData> games = new HashSet<>();
        games.add(new GameData(service.getCurrentGameID(), "game1"));

        service.createGame(sampleAuth, "game1");

        HashSet<GameData> gamesInMem = new HashSet<>(List.of(service.listGames(sampleAuth)));

        assert gamesInMem.equals(games);

        games.add(new GameData(service.getCurrentGameID(), "game2"));
        service.createGame(sampleAuth, "game2");
        gamesInMem = new HashSet<>(List.of(service.listGames(sampleAuth)));

        assert gamesInMem.equals(games);
    }

    @Test
    public void testJoinGameWhite() {
        service.createUser(sampleUser);
        int gameID = service.getCurrentGameID();
        service.createGame(sampleAuth, "Game");
        service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, gameID);
        var game = service.listGames(sampleAuth)[0];

        var testGame = new GameData(gameID, "Game");
        testGame = testGame.setWhiteUsername(sampleAuth.username());

        assert testGame.equals(game);
        assertThrows(Exception.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.WHITE, gameID));
    }

    @Test
    public void testJoinGameBlack() {
        service.createUser(sampleUser);
        int gameID = service.getCurrentGameID();
        service.createGame(sampleAuth, "Game");
        service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID);
        var game = service.listGames(sampleAuth)[0];

        var testGame = new GameData(gameID, "Game");
        testGame = testGame.setBlackUsername(sampleAuth.username());

        assert testGame.equals(game);
        assertThrows(Exception.class, () -> service.joinGame(sampleAuth, ChessGame.TeamColor.BLACK, gameID));
    }

    @Test
    public void testClearData() {
        service.createUser(sampleUser);
        service.createGame(sampleAuth, "");
        service.clearData();

        assert service.getUser(sampleUser.username()) == null;
        assertThrows(Exception.class, () -> service.listGames(sampleAuth));
    }
}
