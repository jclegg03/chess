package dataaccess;

import io.javalin.http.HttpStatus;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public final HttpStatus status;
    public DataAccessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
    public DataAccessException(String message, Throwable ex, HttpStatus status) {
        super(message, ex);
        this.status = status;
    }

}
