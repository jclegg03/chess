package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static ByteArrayOutputStream output = new ByteArrayOutputStream();
    private static String lineEnd = System.lineSeparator();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);

        System.setOut(new PrintStream(output));
    }

    @BeforeEach
    public void reset() {
        serverFacade.clearDatabase();
        output.reset();
    }

    @AfterAll
    static void stopServer() {
        server.stop();

        System.setOut(System.out);
    }


    @Test
    public void loginNegativeTest() {
        var user = new UserData("username", "password", null);
        serverFacade.login(user);
        assertEquals("Username and password do not match." + lineEnd, output.toString());
    }

    @Test
    public void registerPositiveTest() {
        var user = new UserData("username", "password", "email");
        serverFacade.register(user);
        assertEquals("User registered. You are logged in as " + user.username() + "." + lineEnd, output.toString());
    }

    @Test
    public void registerUserTwice() {
        var user = new UserData("username", "password", "email");
        serverFacade.register(user);
        output.reset();
        serverFacade.register(user);
        assertEquals("Username " + user.username() + " is already taken!" + lineEnd, output.toString());
    }

    @Test
    public void logoutPositiveTest() {
        var user = new UserData("username", "password", "email");
        var authToken = serverFacade.register(user);
        var auth = new AuthData(user.username(), authToken);
        output.reset();
        serverFacade.logout(auth);
        assertEquals("Bye " + user.username() + "!" + lineEnd, output.toString());
    }

    @Test
    public void logoutNoOne() {
        serverFacade.logout(new AuthData("", ""));
        assertEquals("Uh, doesn't look like you were even logged in to begin with." + lineEnd,
                output.toString());
    }

    @Test
    public void loginPositiveTest() {
        var user = new UserData("username", "password", "email");
        var auth = serverFacade.register(user);
        serverFacade.logout(new AuthData(user.username(), auth));
        output.reset();
        var newAuth = serverFacade.login(user);
        assertEquals("You are logged in as " + user.username() + "." + lineEnd, output.toString());
        output.reset();
        var otherAuth = serverFacade.login(user);
        assertNotEquals(newAuth, otherAuth);
        assertEquals("You are logged in as " + user.username() + "." + lineEnd, output.toString());
    }
}
