package ui.userstates;

import model.User;
import server.ServerFacade;
import ui.EscapeSequences;

public class GameplayUI extends CLIUserInterface {


    public GameplayUI(User user, ServerFacade serverFacade) {
        super(user, serverFacade);
    }

    @Override
    public CLIUserInterface handleInput(String[] input) {
        CLIUserInterface ui = this;

        switch (input[0].toLowerCase()) {
            case "help" -> help();
            case "redraw" -> redraw();
            case "leave" -> leave();
            case "make", "move" -> makeMove();
            case "resign" -> resign();
            case "highlight" -> highlightLegalMoves();
            default -> defaultInput();
        }

        return ui;
    }

    protected void help() {
        super.help();

        String[] helpStrings = {
                EscapeSequences.SET_TEXT_COLOR_GREEN + "redraw chess board" + EscapeSequences.RESET_TEXT_COLOR
                        + " - prints the chess board again to fix any weird looking boards. Make sure to increase window" +
                        " size if errors persist",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "leave" + EscapeSequences.RESET_TEXT_COLOR
                        + " - exits the game without resigning",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "make move <start position> <end position>" + EscapeSequences.RESET_TEXT_COLOR
                        + " - moves the piece on the start position to the end position, or yells at you " +
                        "if you try something illegal",
                "\t(you will be prompted select a promotion type if a move leads" +
                        " to a pawn promotion)",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "resign" + EscapeSequences.RESET_TEXT_COLOR
                        + " - prompts you to confirm that you want to resign, then ends the game (use leave to quit " +
                        "without ending the game)",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "highlight legal moves <position>" +
                        EscapeSequences.RESET_TEXT_COLOR + " - shows all moves the piece at the specified position can make"
        };

        for (var string : helpStrings) {
            System.out.println(string);
        }
    }

    private void redraw() {

    }

    private void leave() {

    }

    private void makeMove() {

    }

    private void resign() {

    }

    private void highlightLegalMoves() {

    }

    private void defaultInput() {

    }
}
