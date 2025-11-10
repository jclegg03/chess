package model;

public class User {
    private boolean isLoggedIn;
    private String authToken;

    public User() {
        isLoggedIn = false;
        authToken = null;
    }

    public void login(String authToken) {
        isLoggedIn = true;
        this.authToken = authToken;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public String getAuthToken() {
        return authToken;
    }
}
