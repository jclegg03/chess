package ui;

import model.User;
import server.ServerFacade;

public abstract class CLIUserInterface {
    protected final ServerFacade serverFacade;
    protected final User user;

    public CLIUserInterface(User user, ServerFacade serverFacade) {
        this.user = user;
        this.serverFacade = serverFacade;
    }

    public abstract CLIUserInterface handleInput(String[] input);

    protected void printDefaultHelp() {
        var helpString = EscapeSequences.SET_TEXT_COLOR_GREEN + "help" + EscapeSequences.RESET_TEXT_COLOR +
                " - Tells you what commands you can use";
        var quitString = EscapeSequences.SET_TEXT_COLOR_GREEN + "quit" + EscapeSequences.RESET_TEXT_COLOR +
                " - Exits the program";
        System.out.println(helpString);
        System.out.println(quitString);
    }

    protected void invalidInput(String[] inputs) {
        System.out.println("Command \"" + inputs[0] + "\" is not a valid input. Type help for a list of valid" +
                " inputs.");
    }

    protected void invalidInput(String[] inputs, String... expectedFormat) {
        System.out.println("Invalid use of " + inputs[0] + ".");
        System.out.print("Expected: ");
        for (String string : expectedFormat) {
            System.out.print(string + " ");
        }
        System.out.println();
    }
}
