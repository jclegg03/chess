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

    public DataAccessException(String message, Throwable ex) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
