package server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import model.*;
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
        server.delete("session", this::logout);
        server.post("user", this::register);
        server.post("session", this::login);
        server.post("game", this::createGame);
        server.put("game", this::joinGame);
        server.get("game", this::listGames);
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

    private void logout(Context ctx) {
        try {
            service.logout(getAuth(ctx));
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

    private void createGame(Context ctx) {
        try {
            var auth = getAuth(ctx);
            var req = serializer.fromJson(ctx.body(), CreateGameRequest.class);
            var gameID = service.createGame(auth, req.gameName());
            var res = Map.of("gameID", gameID);
            ctx.result(serializer.toJson(res, Map.class));
        }
        catch (ServerException e) {
            handleServerException(e, ctx);
        }
    }

    private void joinGame(Context ctx) {
        try {
            var auth = getAuth(ctx);
            var req = serializer.fromJson(ctx.body(), JoinGameRequest.class);
            service.joinGame(auth,
                    req.playerColor(),
                    req.gameID());
        }
        catch (ServerException e) {
            handleServerException(e, ctx);
        }
    }

    private void listGames(Context ctx) {
        try {
            var auth = getAuth(ctx);
            var games = new GameDataList(service.listGames(auth));

            ctx.result(serializer.toJson(games, GameDataList.class));
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

    private AuthData getAuth(Context ctx) {
        return service.getAuth(ctx.header("authorization"));
    }

    public int run(int desiredPort) {
        server.start(desiredPort);
        return server.port();
    }

    public void stop() {
        server.stop();
    }
}
