package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.DatabaseAuthDAO;
import dataaccess.auth.LocalAuthDAO;
import dataaccess.game.DatabaseGameDAO;
import dataaccess.game.GameDAO;
import dataaccess.game.LocalGameDAO;
import dataaccess.user.DatabaseUserDAO;
import dataaccess.user.LocalUserDAO;
import dataaccess.user.UserDAO;
import io.javalin.http.HttpStatus;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.Server;
import server.ServerException;

import java.util.UUID;

public class Service {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public Service() {
        UserDAO userDAO;
        GameDAO gameDAO;
        AuthDAO authDAO;

        try {
            userDAO = new DatabaseUserDAO();
            authDAO = new DatabaseAuthDAO();
            gameDAO = new DatabaseGameDAO();
        } catch (DataAccessException e) {
            userDAO = new LocalUserDAO();
            gameDAO = new LocalGameDAO();
            authDAO = new LocalAuthDAO();
        }

        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public Service(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }


    private UserData getUser(String username) throws DataAccessException {
        return userDAO.selectUser(username);
    }

    public AuthData createUser(UserData user) throws ServerException {
        if (user.username() == null ||
                user.password() == null ||
                user.email() == null) {
            throw new ServerException("bad request", HttpStatus.BAD_REQUEST);
        }

        UserData data = getUser(user.username());
        if (data == null) {
            userDAO.insertUser(new UserData(user.username(),
                    BCrypt.hashpw(user.password(), BCrypt.gensalt()),
                    user.email()));

            AuthData auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        } else {
            throw new ServerException("already taken", HttpStatus.FORBIDDEN);
        }
    }

    public AuthData login(UserData user) throws ServerException {
        if (user == null ||
                user.username() == null ||
                user.password() == null) {
            throw new ServerException("bad request", HttpStatus.BAD_REQUEST);
        }

        UserData userOnRecord = getUser(user.username());

        if (userOnRecord == null ||
                !BCrypt.checkpw(user.password(), userOnRecord.password())) {
            throw new ServerException("unauthorized", HttpStatus.UNAUTHORIZED);
        } else {
            var auth = makeAuthData(user);
            addAuth(auth);
            return auth;
        }
    }

    public void logout(AuthData auth) throws ServerException {
        if (auth == null ||
                authDAO.selectAuth(auth.authToken()) == null) {
            throw new ServerException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
        }

        authDAO.deleteAuth(auth);
        //TODO Handle logging out/disconnecting while in a game
    }

    public int createGame(AuthData auth, String gameName) throws ServerException {
        checkAuth(auth);
        if (gameName == null) {
            throw new ServerException("Error: bad request", HttpStatus.BAD_REQUEST);
        }
        var game = new GameData(0, gameName);
        return addGame(game);
    }

    public GameData[] listGames(AuthData auth) throws ServerException {
        checkAuth(auth);
        return gameDAO.selectAllGames();
    }

    public GameData joinGame(AuthData auth, ChessGame.TeamColor color, int gameID) throws ServerException {
        checkAuth(auth);
        GameData game = gameDAO.selectGame(gameID);
        if (game == null ||
                color == null) {
            throw new ServerException("Error: bad request", HttpStatus.BAD_REQUEST);
        }

        if (color == ChessGame.TeamColor.WHITE) {
            if (game.whiteUsername() == null) {
                game = game.setWhiteUsername(auth.username());
            } else {
                throw new ServerException("Error: color already taken", HttpStatus.FORBIDDEN);
            }
        } else if (color == ChessGame.TeamColor.BLACK) {
            if (game.blackUsername() == null) {
                game = game.setBlackUsername(auth.username());
            } else {
                throw new ServerException("Error: color already taken", HttpStatus.FORBIDDEN);
            }
        } else {
            throw new ServerException(color + " is an illegal color.", HttpStatus.BAD_REQUEST);
        }
        updateGame(game);
        return game;
    }

    public GameData observeGame(AuthData auth, int gameID) throws ServerException {
        checkAuth(auth);
        GameData game = gameDAO.selectGame(gameID);
        if (game == null) {
            throw new ServerException("Error: bad request (game does not exist)", HttpStatus.BAD_REQUEST);
        }

        game.addObserver(auth.username());
        updateGame(game);
        return game;
    }

    public GameData getGame(AuthData auth, int gameID) throws ServerException {
        checkAuth(auth);
        GameData game = gameDAO.selectGame(gameID);
        if (game == null) {
            throw new ServerException("Error: bad request (game does not exist)", HttpStatus.BAD_REQUEST);
        }

        return game;
    }

    public GameData makeMove(AuthData auth, int gameID, ChessMove move) throws ServerException {
        checkAuth(auth);
        var game = getGame(auth, gameID);
        var turnUsername = game.game().getTeamTurn() == ChessGame.TeamColor.WHITE ?
                game.whiteUsername() : game.blackUsername();
        if(auth.username().equals(turnUsername)) {
            try {
                game.game().makeMove(move);
                var turn = game.game().getTeamTurn();
                var isCheckmate = game.game().isInCheckmate(turn);
                var isStalemate = game.game().isInStalemate(turn);
                game.game().setGameOver(isCheckmate || isStalemate);
                gameDAO.updateGame(gameID, game);
            } catch (InvalidMoveException e) {
                throw new ServerException("Illegal move", HttpStatus.BAD_REQUEST, e);
            }
        }
        else {
            throw new ServerException("Not your turn.", HttpStatus.BAD_REQUEST);
        }

        return game;
    }

    public void resign(AuthData auth, int gameID) throws ServerException {
        checkAuth(auth);
        var game = getGame(auth, gameID);
        if((auth.username().equals(game.blackUsername()) || auth.username().equals(game.whiteUsername())) &&
                !game.game().isGameOver()) {
            game.game().setGameOver(true);
            gameDAO.updateGame(gameID, game);
        } else {
            throw new ServerException("You can't resign.", HttpStatus.BAD_REQUEST);
        }
    }

    public void leaveGame(AuthData auth, int gameID) throws ServerException {
        checkAuth(auth);
        var game = getGame(auth, gameID);
        if(auth.username().equals(game.whiteUsername())) {
            game = game.setWhiteUsername(null);
            gameDAO.updateGame(gameID, game);
        }
        else if(auth.username().equals(game.blackUsername())) {
            game = game.setBlackUsername(null);
            gameDAO.updateGame(gameID, game);
        }
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

    private int addGame(GameData game) throws DataAccessException {
        return gameDAO.insertGame(game);
    }

    private void checkAuth(AuthData auth) throws ServerException {
        if (auth == null) {
            throw new ServerException("Error: unauthorized", HttpStatus.UNAUTHORIZED);
        }

        var authOnRecord = authDAO.selectAuth(auth.authToken());
        if (!auth.equals(authOnRecord)) {
            throw new ServerException("error: unauthorized", HttpStatus.UNAUTHORIZED);
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