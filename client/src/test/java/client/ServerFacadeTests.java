package client;

import model.User;
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
        assertEquals("Logged in as " + user.username() + "." + lineEnd, output.toString());
    }

    @Test
    public void registerUserTwice() {
        var user = new UserData("username", "password", "email");
        serverFacade.register(user);
        serverFacade.register(user);
        assertEquals("Username " + user.username() + " is already taken!" + lineEnd, output.toString());
    }
}
