package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;

import java.util.Map;

public class Server {

    private final Javalin server;
    private final Gson serializer;

    public Server() {
        server = Javalin.create(config -> config.staticFiles.add("web"));
        serializer = new Gson();

        server.delete("db", ctx -> ctx.result("{}"));
        server.post("user", ctx -> register(ctx));
        server.post("session", ctx -> login(ctx));
    }

    private void register(Context ctx) {
        var req = getRequest(ctx);
        var res = Map.of("username", req.get("username"), "authToken", req.get("password").hashCode());

        ctx.result(serializer.toJson(res));
    }

    private void login(Context ctx) {
        var req = getRequest(ctx);
        var res = Map.of("username", req.get("username"), "authToken", req.get("password").hashCode());

        ctx.result(serializer.toJson(res));
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
