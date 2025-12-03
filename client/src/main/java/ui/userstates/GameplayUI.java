package ui.userstates;

import model.User;
import server.ServerFacade;

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
            case "make" -> makeMove();
            case "resign" -> resign();
            case "highlight" -> highlightLegalMoves();
            default -> defaultInput();
        }

        return ui;
    }

    protected void help() {
        super.help();
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
