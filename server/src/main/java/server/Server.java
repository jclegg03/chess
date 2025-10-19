package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import model.UserData;
import service.Service;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final Gson serializer;
    private final Service service;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        serializer = new Gson();
        service = new Service();

        server.delete("db", this::clear);
        server.post("user", this::register);
        server.post("session", this::login);
    }

    private void clear(Context ctx) {
        try {
            service.clearData();
            ctx.result();
        }
        catch (ServerException e) {
            handleServerException(e, ctx);
        }
    }

    private void register(Context ctx) {
        try {
            var user = getUser(ctx);

            var res = service.createUser(user);
            ctx.result(serializer.toJson(res));
        }
        catch (JsonSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result(serializer.toJson(makeErrorMessage("bad request")));
        }
        catch (ServerException e) {
            handleServerException(e, ctx);
        }
    }

    private void login(Context ctx) {
        try {
            var user = getUser(ctx);
            var res = service.login(user);
            ctx.result(serializer.toJson(res));
        }
        catch (ServerException e) {
            handleServerException(e, ctx);
        }
    }

    private void handleServerException(ServerException e, Context ctx) {
        ctx.status(e.getStatus());
        ctx.result(serializer.toJson(makeErrorMessage(e.getMessage())));
    }

    private Map<String, String> makeErrorMessage(String message) {
        return Map.of("message", "Error: " + message);
    }

    private UserData getUser(Context ctx) throws JsonSyntaxException {
        return serializer.fromJson(ctx.body(), UserData.class);
    }

    private Map getRequest(Context ctx) {
        return serializer.fromJson(ctx.body(), Map.class);
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
