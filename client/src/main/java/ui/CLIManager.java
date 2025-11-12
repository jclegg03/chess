package ui;

import chess.ChessGame;
import model.AuthData;
import model.User;
import model.UserData;
import server.ServerFacade;

import java.util.Scanner;

public class CLIManager {
    private final User user;
    private final Scanner scanner;
    private final String[] expectedLogin = {"login", "<username>", "<password>"};
    private final String[] expectedRegister = {"register", "<username>", "<password>", "<email>"};
    private final ServerFacade serverFacade;
    private final String expectedPlay = "play <game id number> <player piece color>";

    public CLIManager(User user, ServerFacade serverFacade) {
        this.user = user;
        this.scanner = new Scanner(System.in);
        this.serverFacade = serverFacade;
    }

    public void repl() {
        while (true) {
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            if (inputs[0].equalsIgnoreCase("quit") || inputs[0].equalsIgnoreCase("exit")) {
                break;
            }
            if (user.isLoggedIn()) {
                loggedIn(inputs);
            } else {
                loggedOut(inputs);
            }
        }
    }

    private void loggedIn(String[] inputs) {
        switch (inputs[0].toLowerCase()) {
            case "help" -> printHelpLoggedIn();
            case "logout" -> logout();
            case "create" -> createGame(inputs);
            case "list" -> listGames();
            case "play", "join" -> joinGame(inputs);
            case "observe" -> observeGame(inputs);
            default -> invalidInput(inputs);
        }
    }

    private void loggedOut(String[] inputs) {
        switch (inputs[0].toLowerCase()) {
            case "help":
                printHelpLoggedOut();
                break;
            case "login":
                if (inputs.length != expectedLogin.length) {
                    invalidInput(inputs, expectedLogin);
                    break;
                }
                login(inputs[1], inputs[2]);
                break;
            case "register":
                if (inputs.length != expectedRegister.length) {
                    invalidInput(inputs, expectedRegister);
                    break;
                }
                register(inputs[1], inputs[2], inputs[3]);
                break;
            default:
                invalidInput(inputs);
                break;
        }
    }

    private void printHelpLoggedIn() {
        printDefaultHelp();
        String[] helpStrings = {
                EscapeSequences.SET_TEXT_COLOR_GREEN + "logout" + EscapeSequences.RESET_TEXT_COLOR
                        + " - logs you out",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "create <game name>" + EscapeSequences.RESET_TEXT_COLOR
                        + " - creates a chess game of the specified name",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "list" + EscapeSequences.RESET_TEXT_COLOR
                        + " - lists all games currently available on the server",

                EscapeSequences.SET_TEXT_COLOR_GREEN + expectedPlay + EscapeSequences.RESET_TEXT_COLOR
                        + " - play the specified game with the specified piece color",

                EscapeSequences.SET_TEXT_COLOR_GREEN + expectedPlay + EscapeSequences.RESET_TEXT_COLOR
                        + " - watch the specified game"
        };

        for (var string : helpStrings) {
            System.out.println(string);
        }
    }

    private void createGame(String[] inputs) {
        if (inputs.length < 2) {
            invalidInput(inputs, "create <game name>");
        }

        var name = new StringBuilder(inputs[1]);
        for (int i = 2; i < inputs.length; i++) {
            name.append(" ");
            name.append(inputs[i]);
        }

        serverFacade.createGame(user.getAuthToken(), name.toString());
    }

    private void joinGame(String[] inputs) {
        try {
            var gameID = validateGameIDInput(inputs, expectedPlay);
            var team = validateTeamColor(inputs);
            serverFacade.joinGame(user.getAuthToken(), gameID, team);
        } catch (RuntimeException e) {
        }
    }

    private void observeGame(String[] inputs) {
        var expected = "observe <game id number>";
        try {
            var gameID = validateGameIDInput(inputs, expected);
            serverFacade.observeGame(user.getAuthToken(), gameID);
        } catch (RuntimeException e) {
        }
    }

    private ChessGame.TeamColor validateTeamColor(String[] inputs) {
        if (inputs.length < 3) {
            invalidInput(inputs, expectedPlay);
        }

        var color = inputs[2];
        if (color.equalsIgnoreCase("white")) {
            return ChessGame.TeamColor.WHITE;
        } else if (color.equalsIgnoreCase("black")) {
            return ChessGame.TeamColor.BLACK;
        } else {
            invalidInput(inputs, expectedPlay);
            System.out.println("Player piece color must be either \"black\" or \"white\". Case doesn't matter.");
            throw new RuntimeException();
        }
    }

    private int validateGameIDInput(String[] inputs, String expected) {
        if (inputs.length < 2) {
            invalidInput(inputs, expected);
        }
        try {
            return Integer.parseInt(inputs[1]);
        } catch (NumberFormatException e) {
            invalidInput(inputs, expected);
            System.out.println("(game id number must be a number like \"1\" or \"7\" but not" +
                    "spelled out like \"nine\"");
            throw new RuntimeException();
        }
    }

    private void listGames() {
        serverFacade.listGames(user.getAuthToken());
    }

    private void logout() {
        serverFacade.logout(new AuthData(user.getUsername(), user.getAuthToken()));
        user.setAuthToken(null);
        user.setUsername(null);
        user.setLoggedIn(false);
    }

    private void register(String username, String password, String email) {
        try {
            user.setAuthToken(serverFacade.register(new UserData(username, password, email)));
            user.setUsername(username);
            user.setLoggedIn(true);
        } catch (RuntimeException e) {
        }
    }

    private void login(String username, String password) {
        try {
            user.setAuthToken(serverFacade.login(new UserData(username, password, null)));
            user.setUsername(username);
            user.setLoggedIn(true);
        } catch (RuntimeException e) {
        }
    }

    private void printHelpLoggedOut() {
        printDefaultHelp();
        var loginString = new StringBuilder(EscapeSequences.SET_TEXT_COLOR_GREEN);
        for (String item : expectedLogin) {
            loginString.append(item);
            loginString.append(" ");
        }
        loginString.append(EscapeSequences.RESET_TEXT_COLOR);
        loginString.append("- to gain access to our premium chess servers and games");
        System.out.println(loginString);

        var registerString = new StringBuilder(EscapeSequences.SET_TEXT_COLOR_GREEN);
        for (String item : expectedRegister) {
            registerString.append(item).append(" ");
        }
        registerString.append(EscapeSequences.RESET_TEXT_COLOR);
        registerString.append("- to create a user and login to our great server");
        System.out.println(registerString);
    }

    private void printDefaultHelp() {
        var helpString = EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR +
                " - Tells you what commands you can use";
        var quitString = EscapeSequences.SET_TEXT_COLOR_GREEN + "quit" + EscapeSequences.RESET_TEXT_COLOR +
                " - Exits the program";
        System.out.println(helpString);
        System.out.println(quitString);
    }

    private void invalidInput(String[] inputs) {
        System.out.println("Command \"" + inputs[0] + "\" is not a valid input. Type help for a list of valid" +
                " inputs.");
    }

    private void invalidInput(String[] inputs, String... expectedFormat) {
        System.out.println("Invalid use of " + inputs[0] + ".");
        System.out.print("Expected: ");
        for (String string : expectedFormat) {
            System.out.print(string + " ");
        }
        System.out.println();
    }
}
