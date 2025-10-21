package server;

import io.javalin.http.HttpStatus;

public class ServerException extends Exception {
    public HttpStatus getStatus() {
        return status;
    }

    protected final HttpStatus status;
    public ServerException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
