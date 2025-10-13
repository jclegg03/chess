package dataaccess.user;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    public void insertUser(UserData user) throws DataAccessException;
    public UserData selectUser(String username) throws DataAccessException;
    public void clearUsers() throws  DataAccessException;
}
