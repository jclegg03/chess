package client;

import chess.ChessBoard;
import chess.ChessGame;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import server.Server;
import server.ServerFacade;
import ui.BoardPrinter;
import ui.EscapeSequences;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static final ByteArrayOutputStream OUTPUT = new ByteArrayOutputStream();
    private final String lineEnd = System.lineSeparator();
    private final UserData user = new UserData("username", "password", "email");
    private AuthData auth;
    private static final PrintStream NORMAL_OUT = System.out;
    private static int port;


    @BeforeAll
    public static void init() {
        PrintStream normalErr = System.err;
        System.setErr(new PrintStream(OUTPUT));
        server = new Server();
        port = server.run(0);
        System.setErr(normalErr);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
        System.setOut(new PrintStream(OUTPUT));
    }

    @BeforeEach
    public void reset() {
        serverFacade.clearDatabase();
        serverFacade = new ServerFacade(port);
        OUTPUT.reset();
    }

    private void createDefaultUser() {
        var authToken = serverFacade.register(user);
        OUTPUT.reset();
        auth = new AuthData(user.username(), authToken);
    }

    @AfterAll
    static void stopServer() {
        server.stop();

        System.setOut(NORMAL_OUT);
    }


    @Test
    public void loginNegativeTest() {
        var user = new UserData("username", "password", null);
        assertThrows(RuntimeException.class, ()-> serverFacade.login(user));
        assertEquals("Username and password do not match." + lineEnd, OUTPUT.toString());
    }

    @Test
    public void registerPositiveTest() {
        var user = new UserData("username", "password", "email");
        serverFacade.register(user);
        assertEquals("User registered. You are logged in as " + user.username() + "." + lineEnd, OUTPUT.toString());
    }

    @Test
    public void registerUserTwice() {
        createDefaultUser();
        serverFacade.register(user);
        assertEquals("Username " + user.username() + " is already taken!" + lineEnd, OUTPUT.toString());
    }

    @Test
    public void logoutPositiveTest() {
        createDefaultUser();
        serverFacade.logout(auth);
        assertEquals("Bye " + user.username() + "!" + lineEnd, OUTPUT.toString());
    }

    @Test
    public void logoutNoOne() {
        serverFacade.logout(new AuthData("", ""));
        assertEquals("Uh, doesn't look like you were even logged in to begin with." + lineEnd,
                OUTPUT.toString());
    }

    @Test
    public void loginPositiveTest() {
        createDefaultUser();
        serverFacade.logout(auth);
        OUTPUT.reset();
        var newAuth = serverFacade.login(user);
        assertEquals("You are logged in as " + user.username() + "." + lineEnd, OUTPUT.toString());
        OUTPUT.reset();
        var otherAuth = serverFacade.login(user);
        assertNotEquals(newAuth, otherAuth);
        assertEquals("You are logged in as " + user.username() + "." + lineEnd, OUTPUT.toString());
    }

    @Test
    public void testListGamesEmpty() {
        createDefaultUser();
        serverFacade.listGames(auth.authToken());
        assertEquals("There are currently 0 games." + lineEnd, OUTPUT.toString());
    }

    @Test
    public void testCreateGame() {
        createDefaultUser();
        var name = "cool game";
        serverFacade.createGame(auth.authToken(), name);
        assertEquals("Game " + name + " was created. It's ID is: 1." + lineEnd, OUTPUT.toString());
    }

    @Test
    public void testCreateAndListManyGames() {
        createDefaultUser();
        var name1 = "cool game";
        var name2 = "Cooler game";

        serverFacade.createGame(auth.authToken(), name1);
        OUTPUT.reset();
        serverFacade.listGames(auth.authToken());
        String expected1 = "There is currently 1 game." + lineEnd + lineEnd +
                name1 + ":" + lineEnd +
                "Game id: " + 1 + lineEnd +
                "White Player: " + lineEnd +
                "Black Player: " + lineEnd +
                "Observers: 0" + lineEnd;

        assertEquals(expected1, OUTPUT.toString());

        serverFacade.createGame(auth.authToken(), name1);
        serverFacade.createGame(auth.authToken(), name2);
        OUTPUT.reset();

        serverFacade.listGames(auth.authToken());

        String expectedMany = "There are currently 3 games." + lineEnd + lineEnd +
                name1 + ":" + lineEnd +
                "Game id: " + 1 + lineEnd +
                "White Player: " + lineEnd +
                "Black Player: " + lineEnd +
                "Observers: 0" + lineEnd +
                lineEnd +
                name1 + ":" + lineEnd +
                "Game id: " + 2 + lineEnd +
                "White Player: " + lineEnd +
                "Black Player: " + lineEnd +
                "Observers: 0" + lineEnd +
                lineEnd +
                name2 + ":" + lineEnd +
                "Game id: " + 3 + lineEnd +
                "White Player: " + lineEnd +
                "Black Player: " + lineEnd +
                "Observers: 0" + lineEnd;

        assertEquals(expectedMany, OUTPUT.toString());
    }

    @ParameterizedTest
    @EnumSource(ChessGame.TeamColor.class)
    public void testJoinGame(ChessGame.TeamColor color) {
        createDefaultUser();
        serverFacade.createGame(auth.authToken(), "game");
        OUTPUT.reset();

        var board = new ChessBoard();
        board.resetBoard();
        BoardPrinter.print(board, color);
        String expectedBoard = OUTPUT.toString();
        OUTPUT.reset();

        serverFacade.joinGame(auth.authToken(), 1, color);
        var team = color == ChessGame.TeamColor.WHITE ? "white" : "black";
        String expected = "Joined game as the " + team + " player." +
                lineEnd +
                expectedBoard;
        assertEquals(expected, OUTPUT.toString());
    }

    @Test
    public void testJoinGameBad() {
        createDefaultUser();
        serverFacade.createGame(auth.authToken(), "game");
        OUTPUT.reset();
        serverFacade.joinGame(auth.authToken(), 1, null);
        assertEquals("You are a traitor and member of the rebel alliance. Take her away." + lineEnd,
                OUTPUT.toString());
    }

    @Test
    public void testJoinGameBadID() {
        createDefaultUser();
        serverFacade.createGame(auth.authToken(), "game");
        OUTPUT.reset();

        serverFacade.joinGame(auth.authToken(), -1, ChessGame.TeamColor.WHITE);
        assertEquals("Bad game ID provided. Use list to get a list of valid game IDs" + lineEnd,
                OUTPUT.toString());
    }

    @Test
    public void testObserveGameBadID() {
        createDefaultUser();
        serverFacade.createGame(auth.authToken(), "game");
        OUTPUT.reset();

        serverFacade.observeGame(auth.authToken(), -1);
        assertEquals("Bad game ID provided. Use list to get a list of valid game IDs" + lineEnd,
                OUTPUT.toString());
    }

    @Test
    public void testObserveGame() {
        createDefaultUser();
        serverFacade.createGame(auth.authToken(), "ga");
        OUTPUT.reset();

        var board = new ChessBoard();
        board.resetBoard();
        BoardPrinter.print(board, ChessGame.TeamColor.WHITE);
        var expected = "You are now observing the epic game between " + EscapeSequences.SET_TEXT_COLOR_YELLOW
                + "awaiting opponent" + EscapeSequences.RESET_TEXT_COLOR + " and " +
                EscapeSequences.SET_TEXT_COLOR_MAGENTA + "awaiting contender" + EscapeSequences.RESET_TEXT_COLOR + "!"
                + lineEnd + OUTPUT;

        OUTPUT.reset();
        serverFacade.observeGame(auth.authToken(), 1);

        assertEquals(expected, OUTPUT.toString());
    }
}
