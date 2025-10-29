package dataaccess.user;

import dataaccess.DataAccessException;
import model.UserData;

public class DatabaseUserDAO implements UserDAO {
    @Override
    public void insertUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData selectUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clearUsers() throws DataAccessException {

    }
}
