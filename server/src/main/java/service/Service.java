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

import java.util.UUID;

public class Service {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private static int currentGameID = 1;


    public Service() {
        userDAO = new LocalUserDAO();
        gameDAO = new LocalGameDAO();
        authDAO = new LocalAuthDAO();
    }


    private UserData getUser(String username) throws DataAccessException {
        return userDAO.selectUser(username);
    }

    public AuthData createUser(UserData user) throws ServerException {
        try {
            assert user.username() != null;
            assert user.password() != null;
            assert user.email() != null;
        } catch (AssertionError e) {
            throw new ServerException("bad request", HttpStatus.BAD_REQUEST);
        }

        UserData data = getUser(user.username());
        if (data == null) {
            userDAO.insertUser(user);

            AuthData auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        } else {
            throw new ServerException("already taken", HttpStatus.FORBIDDEN);
        }
    }

    public AuthData login(UserData user) throws ServerException {
        try {
            assert user != null;
            assert user.username() != null;
            assert user.password() != null;
        } catch (AssertionError e) {
            throw new ServerException("bad request", HttpStatus.BAD_REQUEST);
        }

        UserData userOnRecord = getUser(user.username());

        if (userOnRecord == null ||
                !user.password().equals(userOnRecord.password())) {
            throw new ServerException("unauthorized", HttpStatus.UNAUTHORIZED);
        } else {
            var auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        }
    }

    public void logout(AuthData auth) throws ServerException {
        try {
            assert auth != null;
            assert authDAO.selectAuth(auth.authToken()) != null;
            authDAO.deleteAuth(auth);
        } catch (AssertionError e) {
            throw new ServerException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }

    public int createGame(AuthData auth, String gameName) throws ServerException {
        isAuthorized(auth);
        if(gameName == null) {
            throw new ServerException("Error: bad request", HttpStatus.BAD_REQUEST);
        }
        var game = new GameData(currentGameID, gameName);
        addGame(game);
        return currentGameID++;
    }

    public GameData[] listGames(AuthData auth) throws ServerException {
        isAuthorized(auth);
        return gameDAO.selectAllGames();
    }

    public void joinGame(AuthData auth, ChessGame.TeamColor color, int gameID) throws ServerException {
        isAuthorized(auth);
        GameData game = gameDAO.selectGame(gameID);
        if (color == null ||
                game == null) {
            throw new ServerException("Error: bad request", HttpStatus.BAD_REQUEST);
        }

        if (color == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() == null) {
                game = game.setWhiteUsername(auth.username());
            } else {
                throw new ServerException("Error: color already taken", HttpStatus.FORBIDDEN);
            }
        } else {
            if (game.blackUsername() == null) {
                game = game.setBlackUsername(auth.username());
            } else {
                throw new ServerException("Error: color already taken", HttpStatus.FORBIDDEN);
            }
        }
        updateGame(game);
    }

    public void clearData() throws DataAccessException {
        authDAO.clearAuths();
        gameDAO.clearGames();
        userDAO.clearUsers();
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authDAO.selectAuth(authToken);
    }

    private void updateGame(GameData game) throws DataAccessException {
        gameDAO.updateGame(game.gameID(), game);
    }

    private void addGame(GameData game) throws DataAccessException {
        gameDAO.insertGame(game);

    }

    private void isAuthorized(AuthData auth) throws ServerException {
        try {
            assert auth != null;
            var authOnRecord = authDAO.selectAuth(auth.authToken());
            if (!auth.equals(authOnRecord)) {
                throw new ServerException("error: unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (AssertionError e) {
            throw new ServerException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    private void addAuth(AuthData auth) throws DataAccessException {
        authDAO.insertAuth(auth);
    }

    private AuthData makeAuthData(UserData user) {
        var authToken = UUID.randomUUID().toString();
        return new AuthData(user.username(), authToken);
    }
}