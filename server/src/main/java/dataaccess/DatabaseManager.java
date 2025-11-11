package dataaccess;

import io.javalin.http.HttpStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseManager {
    private static String databaseName;
    private static String dbUsername;
    private static String dbPassword;
    private static String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        loadPropertiesFromResources();
    }

    /**
     * Creates the database if it does not already exist.
     */
    static public void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        try (var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("failed to create database", ex);
        }
    }

    public static void executeVoidStatement(String statement) throws DataAccessException {
        try (var conn = getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("There was a database error", e);
        }
    }

    public static int executeInsertStatement(String statement, String ... args) throws DataAccessException {
        try (var conn = getConnection();
             var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
            for(int i = 1; i <= args.length; i++) {
                preparedStatement.setString(i, args[i-1]);
            }
            preparedStatement.executeUpdate();

            var keys = preparedStatement.getGeneratedKeys();
            if(keys.next()) {
                return keys.getInt(1);
            }

            throw new DataAccessException("There was a database error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (SQLException e) {
            throw new DataAccessException("There was a database error", e);
        }
    }

    public static <T> List<T> executeSelectStatement(String statement, SQLtoJavaInator<T> converter)
            throws DataAccessException {
        try (var conn = getConnection();
             var preparedStatement = conn.prepareStatement(statement)) {
            try (var result = preparedStatement.executeQuery()) {
                var list = new ArrayList<T>();
                while(result.next()) {
                    list.add(converter.convert(result));
                }

                return list;
            }
        } catch (SQLException e) {
            throw new DataAccessException("There was a database error while selecting", e);
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DatabaseManager.getConnection()) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            //do not wrap the following line with a try-with-resources
            var conn = DriverManager.getConnection(connectionUrl, dbUsername, dbPassword);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException ex) {
            throw new DataAccessException("failed to get connection", ex);
        }
    }

    private static void loadPropertiesFromResources() {
        try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
            if (propStream == null) {
                throw new Exception("Unable to load db.properties");
            }
            Properties props = new Properties();
            props.load(propStream);
            loadProperties(props);
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties", ex);
        }
    }

    private static void loadProperties(Properties props) {
        databaseName = props.getProperty("db.name");
        dbUsername = props.getProperty("db.user");
        dbPassword = props.getProperty("db.password");

        var host = props.getProperty("db.host");
        var port = Integer.parseInt(props.getProperty("db.port"));
        connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
    }
}
