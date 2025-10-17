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

        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", this::register);
        server.post("session", this::login);
    }

    private void register(Context ctx) {
        try {
            var user = getUser(ctx);

            assert user.username() != null;
            assert user.password() != null;
            assert user.email() != null;

            var res = service.createUser(user);
            ctx.result(serializer.toJson(res));
        }
        catch (AssertionError | JsonSyntaxException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result(serializer.toJson(makeErrorMessage("bad request")));
        }
        catch (ServerException e) {
            ctx.status(e.getStatus());
            ctx.result(serializer.toJson(makeErrorMessage(e.getMessage())));
        }
    }

    private void login(Context ctx) {
        var req = getRequest(ctx);
        var res = Map.of("username", req.get("username"), "authToken", req.get("password").hashCode());

        ctx.result(serializer.toJson(res));
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
