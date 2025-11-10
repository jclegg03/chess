package server;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Locale;

public class ServerFacade {
    private static final HttpClient client = HttpClient.newHttpClient();
    private final String host;
    private final Gson serializer;
    private final Duration defaultTimeout = Duration.ofMillis(5000);

    public ServerFacade(int port) {
        host = "http://localhost:" + port;
        serializer = new Gson();
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

    public void clearDatabase() {
        try {
            var request = buildRequest("/db", null, null, HTTPMethod.DELETE);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("There was an error clearing the db.");
            throw new RuntimeException(e);
        }
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
