package ui;

import model.User;
import java.util.Scanner;

public class CLIManager {
    private final User user;
    private final Scanner scanner;
    private final String[] expectedLogin = {"login", "<username>", "<password>"};
    private final String[] expectedRegister = {"login", "<username>", "<password>", "<email>"};

    public CLIManager(User user) {
        this.user = user;
        this.scanner = new Scanner(System.in);
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

    }

    private void loggedOut(String[] inputs) {
        switch (inputs[0].toLowerCase()) {
            case "help":
                printHelp();
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

    private void register(String username, String password, String email) {

    }

    private void login(String username, String password) {

    }

    private void printHelp() {

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
