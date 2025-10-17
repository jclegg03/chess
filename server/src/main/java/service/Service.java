package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.LocalAuthDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.LocalGameDAO;
import dataaccess.user.LocalUserDAO;
import dataaccess.user.UserDAO;
import io.javalin.http.HttpStatus;
import model.AuthData;
import model.GameData;
import model.UserData;
import server.ServerException;

import java.util.Objects;

public class Service {
    private final UserDAO USER_DAO;
    private final GameDAO GAME_DAO;
    private final AuthDAO AUTH_DAO;
    private static int currentGameID = 1;


    public Service() {
        USER_DAO = new LocalUserDAO();
        GAME_DAO = new LocalGameDAO();
        AUTH_DAO = new LocalAuthDAO();
    }


    public UserData getUser(String username) {
        try {
            return USER_DAO.selectUser(username);
        }
        catch (DataAccessException e) {
            return null;
        }
    }

    public AuthData createUser(UserData user) throws ServerException {
        UserData data = getUser(user.username());
        if(data == null) {
            try {
                USER_DAO.insertUser(user);
            }
            catch (DataAccessException e) {
                throw new RuntimeException(e);
            }

            AuthData auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        }
        else {
            throw new ServerException("already taken", HttpStatus.FORBIDDEN);
        }
    }

    public AuthData login(UserData user) {
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

    public void logout(AuthData auth) {
        try {
            AUTH_DAO.deleteAuth(auth);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGame(AuthData auth, String gameName) {
        isAuthorized(auth);
        var game = new GameData(currentGameID++, gameName);
        addGame(game);
    }

    public GameData[] listGames(AuthData auth) {
        isAuthorized(auth);
        try {
            return GAME_DAO.selectAllGames();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(AuthData auth, ChessGame.TeamColor color, int gameID) {
        isAuthorized(auth);
        try {
            GameData game = GAME_DAO.selectGame(gameID);
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

    public void clearData() {
        try {
            AUTH_DAO.clearAuths();
            GAME_DAO.clearGames();
            USER_DAO.clearUsers();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateGame(GameData game) {
        try {
            GAME_DAO.updateGame(game.gameID(), game);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void addGame(GameData game) {
        try {
            GAME_DAO.insertGame(game);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void isAuthorized(AuthData auth) {
        try {
            var authOnRecord = AUTH_DAO.selectAuth(auth.authToken());
            if(!auth.equals(authOnRecord)) {
                throw new RuntimeException();
            }
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void addAuth(AuthData auth) {
        try {
            AUTH_DAO.insertAuth(auth);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthData makeAuthData(UserData user) {
        var authToken =  "" + Objects.hash(user.username(), user.password());
        return new AuthData(user.username(), authToken);
    }

    public int getCurrentGameID() {
        return currentGameID;
    }
}
