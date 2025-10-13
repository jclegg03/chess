package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void insertAuth(AuthData auth) throws DataAccessException;
    public void selectAuth(String authToken) throws DataAccessException;
    public void clearAuths() throws  DataAccessException;
}