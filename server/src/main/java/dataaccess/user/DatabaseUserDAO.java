package dataaccess.user;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;


public class DatabaseUserDAO implements UserDAO {
    public DatabaseUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();

        String init = """
                CREATE TABLE IF NOT EXISTS users(
                id INT NOT NULL AUTO_INCREMENT,
                username VARCHAR(100) NOT NULL UNIQUE,
                password VARCHAR(60) NOT NULL,
                email VARCHAR(100) NOT NULL,
                PRIMARY KEY (id),
                INDEX(username)
                )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;""";

        DatabaseManager.executeVoidStatement(init);
    }

    @Override
    public void insertUser(UserData user) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email)\n";
        String values = "VALUES (?, ?, ?);";

        statement += values;

        DatabaseManager.executeInsertStatement(statement, user.username(), user.password(), user.email());
    }

    @Override
    public UserData selectUser(String username) throws DataAccessException {
        String statement = "SELECT password, email FROM users WHERE username = ?;";
        var res = DatabaseManager.executeSelectStatement(statement,
                result -> new UserData(
                        username,
                        result.getString("password"),
                        result.getString("email")
                ), username
        );

        if (res.isEmpty()) {
            return null;
        }

        return res.getFirst();
    }

    @Override
    public void clearUsers() throws DataAccessException {
        DatabaseManager.executeVoidStatement("TRUNCATE users;");
    }
}
