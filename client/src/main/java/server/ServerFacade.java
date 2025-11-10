package server;

import com.google.gson.Gson;
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

    public ServerFacade(int port) {
        host = "http://localhost:" + port;
        serializer = new Gson();
    }

    public void login(UserData user) {
        try {
            String url = String.format(Locale.getDefault(), host + "/session");

            String json = serializer.toJson(user, UserData.class);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .timeout(Duration.ofMillis(5000))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() != 200) {
                System.out.println("Username and password do not match.");
            }

        } catch (URISyntaxException syntaxException) {
            System.out.println("The login function is currently broken. Sorry!");
        } catch (Exception e) {
            System.out.println("There was an error with the server.");
        }
    }
}
