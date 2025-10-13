package dataaccess.user;

import dataaccess.DataAccessException;
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
            if(user.getUsername().equals(username)) return user;
        }

        throw new DataAccessException("There is no user with the username "
                                        + username + ".\n");
    }

    @Override
    public void clearUsers() throws DataAccessException {
        users = new HashSet<>();
    }
}
