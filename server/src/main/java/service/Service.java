package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.LocalAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.LocalGameDAO;
import dataaccess.user.LocalUserDAO;
import dataaccess.user.UserDAO;
import handlers.RegisterHandler;
import kotlin.NotImplementedError;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class Service {
    private static UserDAO userDAO = new LocalUserDAO();
    private static GameDAO gameDAO = new LocalGameDAO();
    private static AuthDAO authDAO = new LocalAuthDAO();

    public static UserData getUser(String username) {
        try {
            return userDAO.selectUser(username);
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public static AuthData createUser(UserData user) {
        UserData data = getUser(user.getUsername());
        if(data == null) {
            try {
                userDAO.insertUser(user);
            }
            catch (DataAccessException e) {
                throw new RuntimeException(e);
            }

            String authToken = "" + Objects.hash(user.getUsername(), user.getPassword());
            AuthData auth = new AuthData(user.getUsername(), authToken);

            try {
                authDAO.insertAuth(auth);
            }
            catch (DataAccessException e) {
                throw new RuntimeException(e);
            }

            return auth;
        }
        else {
            return null;
        }
    }
}
