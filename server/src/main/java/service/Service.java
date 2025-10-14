package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.LocalAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.LocalGameDAO;
import dataaccess.user.LocalUserDAO;
import dataaccess.user.UserDAO;
import kotlin.NotImplementedError;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Objects;

public class Service {
    private static UserDAO userDAO = new LocalUserDAO();
    private static GameDAO gameDAO = new LocalGameDAO();
    private static AuthDAO authDAO = new LocalAuthDAO();
    private static int currentGameID = 1;

    public static UserData getUser(String username) {
        try {
            return userDAO.selectUser(username);
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public static AuthData createUser(UserData user) {
        UserData data = getUser(user.username());
        if(data == null) {
            try {
                userDAO.insertUser(user);
            }
            catch (DataAccessException e) {
                throw new RuntimeException(e);
            }

            AuthData auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        }
        else {
            return null;
        }
    }

    public static AuthData login(UserData user) {
        UserData userOnRecord = getUser(user.username());

        if(!user.equals(userOnRecord)) {
            return null;
        }
        else {
            var auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        }
    }

    public static void logout(AuthData auth) {
        try {
            authDAO.deleteAuth(auth);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createGame(AuthData auth, String gameName) {
        if(!isAuthorized(auth)) {
            throw new RuntimeException();
        }
        else {
            var game = new GameData(currentGameID++, gameName);
            addGame(game);
        }
    }

    private static void addGame(GameData game) {
        try {
            gameDAO.insertGame(game);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isAuthorized(AuthData auth) {
        try {
            var authOnRecord = authDAO.selectAuth(auth.authToken());
            return auth.equals(authOnRecord);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addAuth(AuthData auth) {
        try {
            authDAO.insertAuth(auth);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static AuthData makeAuthData(UserData user) {
        var authToken =  "" + Objects.hash(user.username(), user.password());
        return new AuthData(user.username(), authToken);
    }
}
