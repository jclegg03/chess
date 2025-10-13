package model;

import java.util.Objects;

public class AuthData {
    private final String username;
    private final String authToken;

    public AuthData(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthData authData = (AuthData) o;
        return Objects.equals(username, authData.username) && Objects.equals(authToken, authData.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, authToken);
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
