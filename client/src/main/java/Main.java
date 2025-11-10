import model.User;
import server.ServerFacade;
import ui.CLIManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to chess! Type help for a list of commands, or quit to exit.");
        var cli = new CLIManager(new User(), new ServerFacade(8080));
        cli.repl();

        System.out.println("Goodbye!");
    }
}