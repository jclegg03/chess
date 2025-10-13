package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.user.LocalUserDAO;
import dataaccess.user.UserDAO;
import kotlin.NotImplementedError;
import model.AuthData;
import model.UserData;

public class Service {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public Service() {
        this.userDAO = new LocalUserDAO();
    }

    public UserData getUser(String username) {
        try {
            return userDAO.selectUser(username);
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public AuthData createUser(UserData user) {
        throw new NotImplementedError();
    }
}
