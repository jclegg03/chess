import chess.*;
import model.User;
import ui.BoardPrinter;
import ui.CLIManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to chess! Type help for a list of commands, or quit to exit.");
        var cli = new CLIManager(new User());
        cli.repl();

        System.out.println("Goodbye!");
    }
}