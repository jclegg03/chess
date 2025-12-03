package ui;

import model.User;
import server.ServerFacade;
import ui.userstates.CLIUserInterface;
import ui.userstates.LoggedOutUI;

import java.util.Scanner;

public class CLIManager {
    private final Scanner scanner;

    private CLIUserInterface ui;

    public CLIManager(User user, ServerFacade serverFacade) {
        this.scanner = new Scanner(System.in);

        this.ui = new LoggedOutUI(user, serverFacade);
    }

    public void repl() {
        while (true) {
            String input = scanner.nextLine();
            var inputs = input.split(" ");
            if (inputs[0].equalsIgnoreCase("quit") || inputs[0].equalsIgnoreCase("exit")) {
                //TODO clean up server
                break;
            }

            ui = ui.handleInput(inputs);
        }
    }
}
