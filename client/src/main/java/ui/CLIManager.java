package ui;

import model.AuthData;
import model.User;
import model.UserData;
import server.ServerFacade;

import java.util.Scanner;

public class CLIManager {
    private final User user;
    private final Scanner scanner;
    private final String[] expectedLogin = {"login", "<username>", "<password>"};
    private final String[] expectedRegister = {"login", "<username>", "<password>", "<email>"};
    private final ServerFacade serverFacade;

    public CLIManager(User user, ServerFacade serverFacade) {
        this.user = user;
        this.scanner = new Scanner(System.in);
        this.serverFacade = serverFacade;
    }

    public void repl() {
        while(true) {
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            if(inputs[0].equalsIgnoreCase("quit") || inputs[0].equalsIgnoreCase("exit")) {
                break;
            }
            if(user.isLoggedIn()) {
                loggedIn(inputs);
            }
            else {
                loggedOut(inputs);
            }
        }
    }

    private void loggedIn(String[] inputs) {
        switch (inputs[0].toLowerCase()) {
            case "help" -> printHelpLoggedIn();
            case "logout" -> logout();
            case "create" -> {
                //TODO
            }
            case "list" -> {}//TODO
            case "play" -> {} //TODO
            case "observe" -> {}//TODO
            default -> invalidInput(inputs);
        }
    }

    private void loggedOut(String[] inputs) {
        switch (inputs[0].toLowerCase()) {
            case "help":
                printHelpLoggedOut();
                break;
            case "login":
                if(inputs.length != expectedLogin.length) {
                    invalidInput(inputs, expectedLogin);
                    break;
                }
                login(inputs[1], inputs[2]);
                break;
            case "register":
                if(inputs.length != expectedRegister.length) {
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

    }

    private void createGame(String name) {

    }

    private void logout() {
        serverFacade.logout(new AuthData(user.getUsername(), user.getAuthToken()));
    }

    private void register(String username, String password, String email) {
        user.setAuthToken(serverFacade.register(new UserData(username, password, email)));
        user.setUsername(username);
    }

    private void login(String username, String password) {
        user.setAuthToken(serverFacade.login(new UserData(username, password, null)));
        user.setUsername(username);
    }

    private void printHelpLoggedOut() {
        var helpString = EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR +
                " - Tells you what commands you can use";
        var quitString = EscapeSequences.SET_TEXT_COLOR_GREEN + "quit" + EscapeSequences.RESET_TEXT_COLOR +
                " - Exits the program";
        System.out.println(helpString);
        System.out.println(quitString);
        if(user.isLoggedIn()) {

        }
        else {
            var loginString = new StringBuilder(EscapeSequences.SET_TEXT_COLOR_GREEN);
            for(String item : expectedLogin) {
                loginString.append(item);
                loginString.append(" ");
            }
            loginString.append(EscapeSequences.RESET_TEXT_COLOR);
            loginString.append("- to gain access to our premium chess servers and games");
            System.out.println(loginString);

            var registerString = new StringBuilder(EscapeSequences.SET_TEXT_COLOR_GREEN);
            for(String item : expectedRegister) {
                registerString.append(item).append(" ");
            }
            registerString.append(EscapeSequences.RESET_TEXT_COLOR);
            registerString.append("- to create a user and login to our great server");
            System.out.println(registerString);
        }
    }

    private void invalidInput(String[] inputs) {
            System.out.println("Command " + inputs[0] + " is not a valid input. Type help for a list of valid" +
                    "inputs.");
    }

    private void invalidInput(String[] inputs, String[] expectedFormat) {
        System.out.println("Invalid use of " + inputs[0] + ".");
        System.out.print("Expected: ");
        for(String string : expectedFormat) {
            System.out.print(string + " ");
        }
        System.out.print("\n");
    }
}
