package model;

public class User {
    private boolean isLoggedIn;
    private String authToken;
    private String username;

    public User() {
        isLoggedIn = false;
        authToken = null;
        this.username = null;
    }

    public void login(String username, String authToken) {
        isLoggedIn = true;
        this.authToken = authToken;
        this.username = username;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getAuthToken() {
        return authToken;
    }
}
