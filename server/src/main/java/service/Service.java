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
    private final UserDAO USER_DAO;
    private final GameDAO GAME_DAO;
    private final AuthDAO AUTH_DAO;
    private static int currentGameID = 1;


    public Service() {
        USER_DAO = new LocalUserDAO();
        GAME_DAO = new LocalGameDAO();
        AUTH_DAO = new LocalAuthDAO();
    }


    private UserData getUser(String username) {
        try {
            return USER_DAO.selectUser(username);
        } catch (DataAccessException e) {
            return null;
        }
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
            USER_DAO.insertUser(user);

            AuthData auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        } else {
            throw new ServerException("already taken", HttpStatus.FORBIDDEN);
        }
    }

    public AuthData login(UserData user) throws ServerException {
        try {
            assert user.username() != null;
            assert user.password() != null;
        } catch (AssertionError e) {
            throw new ServerException("bad request", HttpStatus.BAD_REQUEST);
        }

        UserData userOnRecord = getUser(user.username());

        if (userOnRecord == null ||
                !user.username().equals(userOnRecord.username()) ||
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
            assert AUTH_DAO.selectAuth(auth.authToken()) != null;
            AUTH_DAO.deleteAuth(auth);
        } catch (AssertionError e) {
            throw new ServerException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
        }

    }

    public int createGame(AuthData auth, String gameName) throws ServerException {
        try {
            isAuthorized(auth);
            assert gameName != null;
            var game = new GameData(currentGameID, gameName);
            addGame(game);
            return currentGameID++;
        }
        catch (AssertionError e) {
            throw new ServerException("Error: bad request", HttpStatus.BAD_REQUEST);
        }
    }

    public GameData[] listGames(AuthData auth) {
        isAuthorized(auth);
        try {
            return GAME_DAO.selectAllGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void joinGame(AuthData auth, ChessGame.TeamColor color, int gameID) throws ServerException {
        isAuthorized(auth);
        try {
            GameData game = GAME_DAO.selectGame(gameID);
            assert game != null;
            if(color == null) {
                throw new ServerException("Error: bad request", HttpStatus.BAD_REQUEST);
            }

            if (color == ChessGame.TeamColor.WHITE) {
                if (game.whiteUsername() == null)
                    game = game.setWhiteUsername(auth.username());
                else throw new ServerException("Error: color already taken", HttpStatus.FORBIDDEN);
            } else if (color == ChessGame.TeamColor.BLACK) {
                if (game.blackUsername() == null)
                    game = game.setBlackUsername(auth.username());
                else throw new ServerException("Error: color already taken", HttpStatus.FORBIDDEN);
            }

            updateGame(game);
        } catch (AssertionError e) {
            throw new ServerException("Error: Requested game does not exist", HttpStatus.BAD_REQUEST);
        }
    }

    public void clearData() throws DataAccessException {
        AUTH_DAO.clearAuths();
        GAME_DAO.clearGames();
        USER_DAO.clearUsers();
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return AUTH_DAO.selectAuth(authToken);
    }

    private void updateGame(GameData game) {
        try {
            GAME_DAO.updateGame(game.gameID(), game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void addGame(GameData game) {
        try {
            GAME_DAO.insertGame(game);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void isAuthorized(AuthData auth) throws ServerException {
        try {
            assert auth != null;
            var authOnRecord = AUTH_DAO.selectAuth(auth.authToken());
            if (!auth.equals(authOnRecord)) {
                throw new ServerException("error: unauthorized", HttpStatus.UNAUTHORIZED);
            }
        } catch (AssertionError e) {
            throw new ServerException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    private void addAuth(AuthData auth) throws DataAccessException {
        AUTH_DAO.insertAuth(auth);
    }

    private AuthData makeAuthData(UserData user) {
        var authToken = UUID.randomUUID().toString();
        return new AuthData(user.username(), authToken);
    }
}