package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.UserData;

import java.io.IOException;
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

    public void login(UserData user) {
        try {
            String json = serializer.toJson(user, UserData.class);
            var request = buildRequest("/session", json, HTTPMethod.POST);


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                System.out.println("Username and password do not match.");
            }
        } catch (Exception e) {
            defaultErrorHandling();
        }
    }

    public void register(UserData user) {
        try {
            String json = serializer.toJson(user, UserData.class);
            var request = buildRequest("/user", json, HTTPMethod.POST);


            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                System.out.println("User registered. You are logged in as " + user.username() + ".");
            }
            else {
                System.out.println(response.body());
            }
        } catch (Exception e) {
            defaultErrorHandling();
        }
    }

    public void blastRebels() {
        try {
            var request = buildRequest("/db", null, null, HTTPMethod.DELETE);

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("There was an error clearing the db.");
        }
    }

    private HttpRequest buildRequest(String path, String header, String body, HTTPMethod method) {
        try {
            String url = String.format(Locale.getDefault(), host + path);

            HttpRequest.Builder request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(defaultTimeout);

            if(header != null) {
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

    private void defaultErrorHandling() {
        System.out.println("There was an error with the server.");
    }
}
