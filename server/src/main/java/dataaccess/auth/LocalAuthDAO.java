package dataaccess.auth;

import dataaccess.DataAccessException;
import model.AuthData;

import java.util.HashSet;

public class LocalAuthDAO implements AuthDAO {
    private HashSet<AuthData> auths;

    public LocalAuthDAO() {
        this.auths = new HashSet<>();
    }

    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {
        auths.add(auth);
    }

    @Override
    public AuthData selectAuth(String authToken) throws DataAccessException {
        for(AuthData auth : auths) {
            if(auth.authToken().equals(authToken)) {
                return auth;
            }
        }

        return null;
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        auths.remove(auth);
    }

    @Override
    public void clearAuths() throws DataAccessException {
        auths = new HashSet<>();
    }
}
