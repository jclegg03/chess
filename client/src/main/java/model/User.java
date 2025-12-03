package model;

import chess.ChessGame;

public class User {
    private boolean isLoggedIn;
    private String authToken;
    private String username;
    private ChessGame game;
    private ChessGame.TeamColor color;

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

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public void setColor(ChessGame.TeamColor color) {
        this.color = color;
    }
}
