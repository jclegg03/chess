package dataaccess;

import dataaccess.auth.LocalAuthDAO;
import dataaccess.game.LocalGameDAO;
import dataaccess.user.DatabaseUserDAO;
import dataaccess.user.UserDAO;
import model.UserData;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mindrot.jbcrypt.BCrypt;
import server.ServerException;
import service.Service;

public class UserDAOTest {
    private DatabaseUserDAO userDAO;
    private LocalGameDAO gameDAO;
    private LocalAuthDAO authDAO;
    Service service;
    private final UserData user = new UserData("username", "password", "email");

    @BeforeEach
    public void setup() {
        try {
            userDAO = new DatabaseUserDAO();
            userDAO.clearUsers();
            gameDAO = new LocalGameDAO();
            authDAO = new LocalAuthDAO();
            service = new Service(userDAO, gameDAO, authDAO);

        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void cleanUp() {
        try {
            userDAO.clearUsers();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertUserSuccess() {
        try {
            service.createUser(user);
            var res = userDAO.selectUser(user.username());
            assertTrue(BCrypt.checkpw("password", res.password()));
            assertEquals(user.username(), res.username());
            assertEquals(user.email(), res.email());
        } catch (ServerException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertUserFail() {
        try {
            userDAO.insertUser(user);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        assertThrows(DataAccessException.class, () -> userDAO.insertUser(user));
    }

    @Test
    public void selectUserSuccess() {
        try {
            userDAO.insertUser(user);
            assertEquals(user, userDAO.selectUser(user.username()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void selectUserFail() {
        try {
            assertNull(userDAO.selectUser(user.username()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearUsers() {
        try {
            userDAO.insertUser(user);
            userDAO.clearUsers();
            assertNull(userDAO.selectUser(user.username()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
