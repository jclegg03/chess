package model;

public class User {
    private boolean isLoggedIn;
    private String authToken;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
