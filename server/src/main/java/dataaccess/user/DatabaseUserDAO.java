package dataaccess.user;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUserDAO implements UserDAO {
    public DatabaseUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();

        String init = "CREATE TABLE IF NOT EXISTS users(\n" +
                "id INT NOT NULL AUTO_INCREMENT,\n" +
                "username VARCHAR(100) NOT NULL UNIQUE,\n" +
                "password VARCHAR(60) NOT NULL,\n" +
                "email VARCHAR(100) NOT NULL,\n" +
                "PRIMARY KEY (id),\n" +
                "INDEX(username)\n" +
                ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;";

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
