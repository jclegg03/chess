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
        isAuthorized(auth);
        var game = new GameData(currentGameID++, gameName);
        addGame(game);
    }

    public static GameData[] listGames(AuthData auth) {
        isAuthorized(auth);
        try {
            return gameDAO.selectAllGames();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void joinGame(AuthData auth, ChessGame.TeamColor color, int gameID) {
        isAuthorized(auth);
        try {
            GameData game = gameDAO.selectGame(gameID);
            if(color == ChessGame.TeamColor.WHITE) {
                if("".equals(game.whiteUsername()))
                    game = game.setWhiteUsername(auth.username());
                else throw new RuntimeException();
            }
            else if(color == ChessGame.TeamColor.BLACK){
                if("".equals(game.blackUsername()))
                    game = game.setBlackUsername(auth.username());
                else throw new RuntimeException();
            }

            updateGame(game);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateGame(GameData game) {
        try {
            gameDAO.updateGame(game.gameID(), game);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
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

    public static int getCurrentGameID() {
        return currentGameID;
    }

    private static void isAuthorized(AuthData auth) {
        try {
            var authOnRecord = authDAO.selectAuth(auth.authToken());
            if(!auth.equals(authOnRecord)) {
                throw new RuntimeException();
            }
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
