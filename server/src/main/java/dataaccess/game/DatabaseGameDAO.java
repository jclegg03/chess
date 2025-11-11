package dataaccess.game;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;
import serializer.Serializer;

public class DatabaseGameDAO implements GameDAO {
    private final Gson serializer;

    public DatabaseGameDAO() throws DataAccessException {
        this.serializer = Serializer.serializer();

        String init = """
                CREATE TABLE IF NOT EXISTS game(
                id INT UNIQUE AUTO_INCREMENT,
                name VARCHAR(100) NOT NULL,
                white_username VARCHAR(100) DEFAULT NULL,
                black_username VARCHAR(100) DEFAULT NULL,
                game_data JSON NOT NULL,
                num_observers INT DEFAULT 0,
                PRIMARY KEY (id),
                INDEX(name)
                )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;""";

        DatabaseManager.executeVoidStatement(init);
    }

    private String gameToJSON(ChessGame game) {
        return serializer.toJson(game);
    }

    private String addEscapes(String string) {
        if(string == null) {
            return null;
        }

        String result = "";
        for(char c : string.toCharArray()) {
            if(c == '\'') {
                result += "\\";
            }
            result += c;
        }

        return result;
    }

    @Override
    public int insertGame(GameData game) throws DataAccessException {
        String statement = "INSERT INTO game (name, white_username, black_username, game_data)\n" +
                "VALUES (?, ?, ?, ?);";

        return DatabaseManager.executeInsertStatement(statement, game.gameName(), game.whiteUsername(),
                game.blackUsername(), gameToJSON(game.game()));
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        String statement = "UPDATE game SET white_username = ?, black_username = ?, " +
                "game_data = ?, num_observers = ? " +
                "WHERE id = ?;";

        DatabaseManager.executeVoidStatement(statement, game.whiteUsername(),
                game.blackUsername(), gameToJSON(game.game()), game.numObservers(), gameID);
    }

    @Override
    public GameData selectGame(int gameID) throws DataAccessException {
        String statement = "SELECT name, white_username, black_username, game_data, num_observers FROM game WHERE id = '" +
                gameID + "';";

        var res = DatabaseManager.executeSelectStatement(statement, result -> new GameData(
                gameID,
                fixNullUser(result.getString("white_username")),
                fixNullUser(result.getString("black_username")),
                result.getString("name"),
                getGameFromString(result.getString("game_data")),
                result.getInt("num_observers")
                )
        );

        if(res.isEmpty()) {
            return null;
        }

        return res.getFirst();
    }

    private ChessGame getGameFromString(String game) {
        return serializer.fromJson(game, ChessGame.class);
    }

    @Override
    public GameData[] selectAllGames() throws DataAccessException {
        String statement = "SELECT * FROM game;";

        var res = DatabaseManager.executeSelectStatement(statement, result -> new GameData(
                result.getInt("id"),
                fixNullUser(result.getString("white_username")),
                fixNullUser(result.getString("black_username")),
                result.getString("name"),
                getGameFromString(result.getString("game_data")),
                result.getInt("num_observers")
            )
        );

        return res.toArray(new GameData[0]);
    }

    private String fixNullUser(String user) {
        if("null".equals(user)) {
            return null;
        }
        else
        {
            return user;
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        DatabaseManager.executeVoidStatement("TRUNCATE game;");
    }
}
