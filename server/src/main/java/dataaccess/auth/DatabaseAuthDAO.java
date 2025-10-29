package dataaccess.auth;

import dataaccess.DataAccessException;
import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO {
    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public AuthData selectAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {

    }

    @Override
    public void clearAuths() throws DataAccessException {

    }
}
