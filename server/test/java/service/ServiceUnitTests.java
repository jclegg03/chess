package service;

import org.junit.jupiter.api.*;
import model.UserData;

public class ServiceUnitTests {

    private final static Service service = new Service();

    @Test
    public void getUserSuccess() {
        UserData user = service.getUser("username");

        assert user == null;
    }
}
