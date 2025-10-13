package service;

import model.AuthData;
import org.junit.jupiter.api.*;
import model.UserData;

import java.util.Objects;

public class ServiceUnitTests {

    private final static Service service = new Service();

    @Test
    public void createUserSuccess() {
        UserData sampleUser = new UserData("username", "password", "email@mail.com");
        AuthData sampleAuth = new AuthData("username", "" + Objects.hash("username", "password"));
        AuthData auth = service.createUser(sampleUser);

        assert auth.equals(sampleAuth);
    }

    @Test
//      Unfortunately it is impossible to test getUser without first implementing addUser.
    public void getUserSuccess() {
        UserData user = service.getUser("username");
    }
}
