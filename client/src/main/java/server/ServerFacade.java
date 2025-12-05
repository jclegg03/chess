package server;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import serializer.Serializer;
import ui.EscapeSequences;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
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

    public String getHost() {
        return this.host;
    }

    private enum HTTPMethod {GET, POST, DELETE, PUT}

    public String login(UserData user) {
        String json = serializer.toJson(user, UserData.class);
        var request = buildRequest("/session", json);

        //TODO Should this return a list of games too?

        HttpResponse<String> response = makeRequest(request);
        if(response == null) {
            printServerDown();
            throw new RuntimeException();
        }

        if (response.statusCode() != 200) {
            System.out.println("Username and password do not match.");
            throw new RuntimeException();
        }

        System.out.println("You are logged in as " + user.username() + ".");
        return serializer.fromJson(response.body(), AuthData.class).authToken();
    }

    public String register(UserData user) {
        String json = serializer.toJson(user, UserData.class);
        var request = buildRequest("/user", json);


        HttpResponse<String> response = makeRequest(request);
        if(response == null) {
            printServerDown();
            throw new RuntimeException();
        }

        if (response.statusCode() == 200) {
            System.out.println("User registered. You are logged in as " + user.username() + ".");
            return serializer.fromJson(response.body(), AuthData.class).authToken();
        } else if (response.statusCode() == 403) {
            System.out.println("Username " + user.username() + " is already taken!");
            throw new RuntimeException();
        }

        return null;
    }

    public void logout(AuthData auth) {
        var request = buildRequest("/session", auth.authToken(), null, HTTPMethod.DELETE);

        var response = makeRequest(request);
        if(response == null) {
            printServerDown();
            return;
        }

        if (response.statusCode() == 200) {
            System.out.println("Bye " + auth.username() + "!");
        } else {
            System.out.println("Uh, doesn't look like you were even logged in to begin with.");
        }
    }

    public void listGames(String authToken) {
        var request = buildRequest("/game", authToken, null, HTTPMethod.GET);

        var response = makeRequest(request);
        if(response == null) {
            printServerDown();
            return;
        }
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
                var clientGameID = getClientGameID(game.gameID());
                System.out.println("Game id: " + clientGameID);
                var whiteName = game.whiteUsername() == null ? "" : game.whiteUsername();
                System.out.println("White Player: " + whiteName);
                var blackName = game.blackUsername() == null ? "" : game.whiteUsername();
                System.out.println("Black Player: " + blackName);
                System.out.println("Observers: " + game.observers().size());
            }
        }
    }

    private int getClientGameID(int serverGameID) {
        var clientGameID = serverGameIDtoClientGameIDMap.get(serverGameID);
        if(clientGameID == null) {
            return remapGameID(serverGameID);
        }
        return clientGameID;
    }

    public void createGame(String authToken, String name) {
        var json = serializer.toJson(new CreateGameRequest(name), CreateGameRequest.class);
        var request = buildRequest("/game", authToken, json, HTTPMethod.POST);

        var response = makeRequest(request);
        if(response == null) {
            printServerDown();
            return;
        }
        if (response.statusCode() == 200) {
            var gameID = serializer.fromJson(response.body(), GameID.class);
            int id = remapGameID(gameID.gameID());
            System.out.println("Game " + name + " was created. It's ID is: " + id + ".");
        }
    }
    
    public JoinGameResponse joinGame(String authToken, int clientGameID, ChessGame.TeamColor color) {
        if(color == null) {
            return new JoinGameResponse(false, null,
                    "You are a traitor and member of the rebel alliance. Take her away.");
        }
        String team = "black";
        if(color == ChessGame.TeamColor.WHITE) {
            team = "white";
        }

        var serverGameID = clientGameIDtoServerGameIDMap.get(clientGameID);

        if(serverGameID == null) {
            return new JoinGameResponse(false, null,
                    "Bad game ID provided. Use list to get a list of valid game IDs");
        }

        var json = serializer.toJson(new JoinGameRequest(color, serverGameID));
        var request = buildRequest("/game", authToken, json, HTTPMethod.PUT);

        var response = makeRequest(request);
        if(response == null) {
            return new JoinGameResponse(false, null,
                    "The server is currently down or took too long to respond.");
        }
        if(response.statusCode() == 200) {
            var game = serializer.fromJson(response.body(), GameData.class);
            return new JoinGameResponse(true, game.game(),
                    "Joined " + game.gameName() + " as the " + team + " player.", serverGameID);
        }
        else if(response.statusCode() == 403) {
            return new JoinGameResponse(false, null,
                    "Someone is already playing as " + team + ". Use list to see which games don't" +
                    " have players.");
        }
        else {
            return new JoinGameResponse(false, null, "There was an error.");
        }
    }

    public JoinGameResponse observeGame(String authToken, int clientGameID) {
        var serverGameID = clientGameIDtoServerGameIDMap.get(clientGameID);

        if(serverGameID == null) {
            return new JoinGameResponse(false, null,
                    "Bad game ID provided. Use list to get a list of valid game IDs");
        }

        var json = serializer.toJson(new JoinGameRequest(serverGameID));
        var request = buildRequest("/observe", authToken, json, HTTPMethod.PUT);

        var response = makeRequest(request);
        if(response == null) {
            return new JoinGameResponse(false, null,
                    "The server is currently down or took too long to respond.");
        }
        if(response.statusCode() == 200) {
            var game = serializer.fromJson(response.body(), GameData.class);
            return new JoinGameResponse(true, game.game(), getObserveText(game), serverGameID);
        }

        return new JoinGameResponse(false, null, "There was an error");
    }

    private String getObserveText(GameData game) {
        var whiteName = game.whiteUsername() == null? "awaiting opponent" : game.whiteUsername();
        var blackName = game.blackUsername() == null? "awaiting contender" : game.blackUsername();
        return "You are now observing the epic game between " + EscapeSequences.SET_TEXT_COLOR_YELLOW
                + whiteName + EscapeSequences.RESET_TEXT_COLOR + " and " +
                EscapeSequences.SET_TEXT_COLOR_MAGENTA + blackName + EscapeSequences.RESET_TEXT_COLOR + "!";
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

    private HttpRequest buildRequest(String path, String body) {
        return buildRequest(path, null, body, HTTPMethod.POST);
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

    private void printServerDown() {
        System.out.println("The server is currently down or took too long to respond.");
    }
}
