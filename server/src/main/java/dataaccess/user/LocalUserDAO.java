package dataaccess.user;

import dataaccess.DataAccessException;
import io.javalin.http.HttpStatus;
import model.UserData;

import java.util.HashSet;

public class LocalUserDAO implements UserDAO{
    private HashSet<UserData> users;

    public LocalUserDAO() {
        users = new HashSet<>();
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        users.add(user);
    }

    @Override
    public UserData selectUser(String username) throws DataAccessException {
        for(UserData user: users) {
            if(user.username().equals(username)) return user;
        }
        return null;

//        throw new DataAccessException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
    }

    @Override
    public void clearUsers() throws DataAccessException {
        users = new HashSet<>();
    }
}
