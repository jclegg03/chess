package ui;

import model.User;

import java.util.Arrays;
import java.util.Scanner;

public class CLIManager {
    private User user;
    private Scanner scanner;

    public CLIManager(User user) {
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    public void repl() {
        while(true) {
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            System.out.println(Arrays.toString(inputs));
            if(inputs[0].equalsIgnoreCase("quit")) {
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
                if(inputs.length != 4) {
                    invalidInput(inputs);
                    break;
                }
                login(inputs[1], inputs[2], inputs[3]);
                break;
            case "register":
                if(inputs.length != 4) {
                    invalidInput(inputs);
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

    private void login(String username, String password, String email) {

    }

    private void printHelp() {

    }

    private void invalidInput(String[] inputs) {

    }
}
