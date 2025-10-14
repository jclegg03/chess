package service;

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

    @Test
    public void testCreateUser() {
        AuthData auth = Service.createUser(sampleUser);
        assert auth.equals(sampleAuth);
    }

    // Unfortunately it is impossible to test getUser without first implementing createUser.
    @Test
    public void testGetUser() {
        assert Service.getUser("username") == null;

        Service.createUser(sampleUser);
        assert Service.getUser(sampleUser.username()).equals(sampleUser);
    }

    //Again impossible to test without implementing createUser
    @Test
    public void testLogin() {
        assert Service.login(sampleUser) == null;

        AuthData authFromCreate = Service.createUser(sampleUser);
        AuthData authFromLogin = Service.login(sampleUser);

        assert sampleAuth.equals(authFromCreate) && sampleAuth.equals(authFromLogin);
    }

    //Best to test this one after writing functions that check auth status.
    @Test
    public void testLogout() {
        //attempt to logout with no one logged in. This should just do nothing.
        Service.logout(sampleAuth);

        //try doing somethign with an auth that has been logged out
        var expiredAuth = Service.login(sampleUser);
        Service.logout(expiredAuth);

        assertThrows(RuntimeException.class, () -> Service.createGame(expiredAuth, "Cool Game"));
    }

    @Test
    public void testCreateAndListGames() {
        Service.createUser(sampleUser);

        HashSet<GameData> games = new HashSet<>();
        games.add(new GameData(Service.getCurrentGameID(), "game1"));

        Service.createGame(sampleAuth, "game1");

        HashSet<GameData> gamesInMem = new HashSet<>();
        gamesInMem.addAll(List.of(Service.listGames(sampleAuth)));

        assert gamesInMem.equals(games);

        games.add(new GameData(Service.getCurrentGameID(), "game2"));
        Service.createGame(sampleAuth, "game2");
        gamesInMem = new HashSet<>(List.of(Service.listGames(sampleAuth)));

        assert gamesInMem.equals(games);
    }
}
