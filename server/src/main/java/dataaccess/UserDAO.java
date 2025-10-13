package dataaccess;

import model.UserData;

public interface UserDAO {
    public void insertUser(UserData user) throws DataAccessException;
    public void selectUser(String userName) throws DataAccessException;
    public void clearUsers() throws  DataAccessException;
}
