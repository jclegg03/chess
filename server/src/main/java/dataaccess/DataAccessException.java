package dataaccess;

import io.javalin.http.HttpStatus;
import server.ServerException;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends ServerException {
    public DataAccessException(String message, HttpStatus status) {
        super(message, status);
    }
}
