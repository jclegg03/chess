package dataaccess;

import dataaccess.auth.DatabaseAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

class AuthDAOTest {
    DatabaseAuthDAO authDAO;
    String authToken = UUID.randomUUID().toString();
    AuthData auth = new AuthData("username", authToken);

    {
        try {
            authDAO = new DatabaseAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setup() {
        try {
            authDAO.clearAuths();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertAuthSuccess() {
        try {
            authDAO.insertAuth(auth);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertAuthFail() {
        try {
            authDAO.insertAuth(auth);
            assertThrows(DataAccessException.class, () -> authDAO.insertAuth(auth));
            assertThrows(DataAccessException.class, () -> authDAO.insertAuth(new AuthData("username", "")));
            assertThrows(DataAccessException.class, () -> authDAO.insertAuth(new AuthData("", authToken)));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void selectAuthSuccess() {
        try {
            authDAO.insertAuth(auth);
            assertEquals(auth, authDAO.selectAuth(authToken));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void selectAuthFail() {
        try {
            assertNull(authDAO.selectAuth(authToken));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteAuthSuccess() {
        try {
            authDAO.insertAuth(auth);
            authDAO.deleteAuth(auth);
            assertNull(authDAO.selectAuth(authToken));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void deleteAuthFail() {
        try {
            authDAO.deleteAuth(auth);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearAuths() {
        try {
            authDAO.clearAuths();
            authDAO.insertAuth(auth);
            authDAO.clearAuths();
            assertNull(authDAO.selectAuth(authToken));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}