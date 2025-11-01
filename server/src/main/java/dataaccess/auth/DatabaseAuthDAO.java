package dataaccess.auth;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

public class DatabaseAuthDAO implements AuthDAO {
    public DatabaseAuthDAO() throws DataAccessException {
        String init = """
                CREATE TABLE IF NOT EXISTS auth(
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(100) NOT NULL,
                token CHAR(36) NOT NULL UNIQUE,
                PRIMARY KEY (id),
                INDEX(token)
                )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;""";

        DatabaseManager.executeVoidStatement(init);
    }

    @Override
    public void insertAuth(AuthData auth) throws DataAccessException {
        String statement = "INSERT INTO auth (username, token)\n";
        String values = "VALUES ('";

        values += auth.username() + "', '";
        values += auth.authToken() + "');";

        statement += values;

        DatabaseManager.executeVoidStatement(statement);
    }

    @Override
    public AuthData selectAuth(String authToken) throws DataAccessException {
        String statement = "SELECT username FROM auth WHERE token = '" +
                authToken + "';";
        var res = DatabaseManager.executeSelectStatement(statement,
                result -> new AuthData(
                        result.getString("username"),
                        authToken
                ));

        if(res.isEmpty()) {
            return null;
        }

        return res.getFirst();
    }

    @Override
    public void deleteAuth(AuthData auth) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE token = '"
                + auth.authToken() + "';";

        DatabaseManager.executeVoidStatement(statement);
    }

    @Override
    public void clearAuths() throws DataAccessException {
        DatabaseManager.executeVoidStatement("TRUNCATE auth;");
    }
}
