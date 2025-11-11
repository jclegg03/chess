package server;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import model.*;
import serializer.Serializer;
import ui.BoardPrinter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;

public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();
    private final String host;
    private final Gson serializer;
    private final Duration defaultTimeout = Duration.ofMillis(5000);
    private final HashMap<Integer, Integer> clientGameIDtoServerGameIDMap;
    private final HashMap<Integer, Integer> serverGameIDtoClientGameIDMap;


    public ServerFacade(int port) {
        host = "http://localhost:" + port;
        serializer = Serializer.serializer();
        clientGameIDtoServerGameIDMap = new HashMap<>();
        serverGameIDtoClientGameIDMap = new HashMap<>();
    }

    private enum HTTPMethod {GET, POST, DELETE, PUT}

    public String login(UserData user) {
        String json = serializer.toJson(user, UserData.class);
        var request = buildRequest("/session", json, HTTPMethod.POST);


        HttpResponse<String> response = makeRequest(request);

        if (response.statusCode() != 200) {
            System.out.println("Username and password do not match.");
            return null;
        }

        System.out.println("You are logged in as " + user.username() + ".");
        return serializer.fromJson(response.body(), AuthData.class).authToken();
    }

    public String register(UserData user) {
        String json = serializer.toJson(user, UserData.class);
        var request = buildRequest("/user", json, HTTPMethod.POST);


        HttpResponse<String> response = makeRequest(request);

        if (response.statusCode() == 200) {
            System.out.println("User registered. You are logged in as " + user.username() + ".");
            return serializer.fromJson(response.body(), AuthData.class).authToken();
        } else if (response.statusCode() == 403) {
            System.out.println("Username " + user.username() + " is already taken!");
        }

        return null;
    }

    public void logout(AuthData auth) {
        var request = buildRequest("/session", auth.authToken(), null, HTTPMethod.DELETE);

        var response = makeRequest(request);
        if (response.statusCode() == 200) {
            System.out.println("Bye " + auth.username() + "!");
        } else {
            System.out.println("Uh, doesn't look like you were even logged in to begin with.");
        }
    }

    public void listGames(String authToken) {
        var request = buildRequest("/game", authToken, null, HTTPMethod.GET);

        var response = makeRequest(request);
        if (response.statusCode() == 200) {
            var games = serializer.fromJson(response.body(), GameDataList.class);
            if (games.games().length == 1) {
                System.out.println("There is currently 1 game.");
            }
            else {
                System.out.println("There are currently " + games.games().length + " games.");
            }

            for (GameData game : games.games()) {
                System.out.println();
                System.out.println(game.gameName() + ":");
                System.out.println("Game id: " + serverGameIDtoClientGameIDMap.get(game.gameID()));
                var whiteName = game.whiteUsername() == null ? "" : game.whiteUsername();
                System.out.println("White Player: " + whiteName);
                var blackName = game.blackUsername() == null ? "" : game.whiteUsername();
                System.out.println("Black Player: " + blackName);
                //TODO fix hard coding 0 here.
                System.out.println("Observers: 0");
            }
        }
    }

    public void createGame(String authToken, String name) {
        var json = serializer.toJson(new CreateGameRequest(name), CreateGameRequest.class);
        var request = buildRequest("/game", authToken, json, HTTPMethod.POST);

        var response = makeRequest(request);
        if (response.statusCode() == 200) {
            var gameID = serializer.fromJson(response.body(), GameID.class);
            int id = remapGameID(gameID.gameID());
            System.out.println("Game " + name + " was created. It's ID is: " + id + ".");
        }
    }
    
    public void joinGame(String authToken, int clientGameID, ChessGame.TeamColor color) {
        String team = "black";
        if(color == ChessGame.TeamColor.WHITE) {
            team = "white";
        }

        var json = serializer.toJson(new JoinGameRequest(color, clientGameIDtoServerGameIDMap.get(clientGameID)));
        var request = buildRequest("/game", authToken, json, HTTPMethod.PUT);

        var response = makeRequest(request);
        if(response.statusCode() == 200) {
            var game = serializer.fromJson(response.body(), GameData.class);
            System.out.println("Joined " + game.gameName() + " as the " + team + " player.");
            BoardPrinter.print(game.game().getBoard(), color);
        }
    }

    public void clearDatabase() {
        try {
            var request = buildRequest("/db", null, null, HTTPMethod.DELETE);

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("There was an error clearing the db.");
            throw new RuntimeException(e);
        }
    }

    private int remapGameID(int serverGameID) {
        int currentID = 1;
        while (clientGameIDtoServerGameIDMap.get(currentID) != null) {
            currentID++;
        }
        clientGameIDtoServerGameIDMap.put(currentID, serverGameID);
        serverGameIDtoClientGameIDMap.put(serverGameID, currentID);
        return currentID;
    }

    private HttpRequest buildRequest(String path, String header, String body, HTTPMethod method) {
        try {
            String url = String.format(Locale.getDefault(), host + path);

            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(defaultTimeout);

            if (header != null) {
                request.header("authorization", header);
            }

            switch (method) {
                case GET:
                    request.GET();
                    break;
                case PUT:
                    request.PUT(HttpRequest.BodyPublishers.ofString(body));
                    break;
                case POST:
                    request.POST(HttpRequest.BodyPublishers.ofString(body));
                    break;
                case DELETE:
                    request.DELETE();
                    break;
            }

            return request.build();
        } catch (URISyntaxException e) {
            System.out.println("That command is currently broken. Sorry!");
        }

        return null;
    }

    private HttpRequest buildRequest(String path, String body, HTTPMethod method) {
        return buildRequest(path, null, body, method);
    }

    private HttpResponse<String> makeRequest(HttpRequest request) {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            defaultErrorHandling();
        }
        return null;
    }

    private void defaultErrorHandling() {
        System.out.println("There was an error with the server.");
    }
}
