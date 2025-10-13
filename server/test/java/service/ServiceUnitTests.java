package service;

import model.AuthData;
import org.junit.jupiter.api.*;
import model.UserData;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceUnitTests {

    private final static Service service = new Service();
    UserData sampleUser = new UserData("username", "password", "email@mail.com");
    AuthData sampleAuth = new AuthData("username", "" + Objects.hash("username", "password"));

    @Test
    public void createUserSuccess() {
        AuthData auth = Service.createUser(sampleUser);
        assert auth.equals(sampleAuth);
    }

    @Test
//      Unfortunately it is impossible to test getUser without first implementing addUser.
    public void getUserSuccess() {
        assert Service.getUser("username") == null;

        Service.createUser(sampleUser);
        assert Service.getUser(sampleUser.username()).equals(sampleUser);
    }
}
