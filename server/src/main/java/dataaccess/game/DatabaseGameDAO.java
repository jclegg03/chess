package dataaccess.game;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.GameData;

import java.io.IOException;
import java.util.HashMap;

public class DatabaseGameDAO implements GameDAO {
    private Gson serializer;

    public DatabaseGameDAO() throws DataAccessException {
        this.serializer = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<HashMap<ChessPosition, ChessPiece>>(){}.getType(),
                        new TypeAdapter<HashMap<ChessPosition, ChessPiece>>() {
                            @Override
                            public void write(JsonWriter out, HashMap<ChessPosition, ChessPiece> map) throws IOException {
                                out.beginObject();
                                for (var entry : map.entrySet()) {
                                    out.name(entry.getKey().toString());
                                    serializer.toJson(entry.getValue(), ChessPiece.class, out);
                                }
                                out.endObject();
                            }

                            @Override
                            public HashMap<ChessPosition, ChessPiece> read(JsonReader in) throws IOException {
                                HashMap<ChessPosition, ChessPiece> map = new HashMap<>();
                                in.beginObject();
                                while (in.hasNext()) {
                                    String key = in.nextName();
                                    ChessPiece value = serializer.fromJson(in, ChessPiece.class);
                                    map.put(ChessPosition.fromString(key), value);
                                }
                                in.endObject();
                                return map;
                            }
                        }
                )
                .serializeNulls()
                .create();

        String init = "CREATE TABLE IF NOT EXISTS game(\n" +
                "id INT NOT NULL,\n" +
                "name VARCHAR(100) NOT NULL UNIQUE,\n" +
                "white_username VARCHAR(100) DEFAULT NULL,\n" +
                "black_username VARCHAR(100) DEFAULT NULL,\n" +
                "game_data JSON NOT NULL,\n" +
                "PRIMARY KEY (id),\n" +
                "INDEX(name)\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

        DatabaseManager.executeVoidStatement(init);
    }

    private String gameToJSON(ChessGame game) {
        return serializer.toJson(game);
    }

    @Override
    public void insertGame(GameData game) throws DataAccessException {
        String statement = "INSERT INTO game (id, name, white_username, black_username, game_data)\n";
        String values = "VALUES ('";

        values += game.gameID() + "', '" +
                  game.gameName() + "', '" +
                  game.whiteUsername() + "', '" +
                  game.blackUsername() + "', '" +
                  gameToJSON(game.game()) + "');";

        statement += values;

        DatabaseManager.executeVoidStatement(statement);
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        String statement = "UPDATE game SET white_username = '" +
                game.whiteUsername() + "', black_username = '" +
                game.blackUsername() + "', game_data = '" +
                game.game() + "' " +
                "WHERE id = '" + gameID + "';";

        DatabaseManager.executeVoidStatement(statement);
    }

    @Override
    public GameData selectGame(int gameID) throws DataAccessException {
        String statement = "SELECT name, white_username, black_username, game_data FROM game WHERE id = '" +
                gameID + "';";

        var res = DatabaseManager.executeSelectStatement(statement, result -> new GameData(
                gameID,
                fixNullUser(result.getString("white_username")),
                fixNullUser(result.getString("black_username")),
                result.getString("name"),
                getGameFromString(result.getString("game_data"))
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
                getGameFromString(result.getString("game"))
            )
        );

        return res.toArray(new GameData[0]);
    }

    private String fixNullUser(String user) {
        if("null".equals(user)) {
            return null;
        }
        else return user;
    }

    @Override
    public void clearGames() throws DataAccessException {
        DatabaseManager.executeVoidStatement("TRUNCATE game;");
    }
}
