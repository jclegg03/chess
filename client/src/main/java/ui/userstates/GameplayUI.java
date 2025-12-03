package ui.userstates;

import chess.ChessMove;
import chess.ChessPosition;
import model.User;
import server.ServerFacade;
import ui.BoardPrinter;
import ui.EscapeSequences;

import java.util.ArrayList;

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
            case "make", "move" -> makeMove(input);
            case "resign" -> resign();
            case "highlight" -> highlightLegalMoves(input);
            default -> defaultInput(input);
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
        BoardPrinter.print(user.getGame().getBoard(), user.getPerspective());
    }

    private void leave() {

    }

    private void makeMove(String[] input) {

    }

    private void resign() {

    }

    private void highlightLegalMoves(String[] input) {
        if(input.length < 2) {
            invalidInput(input, "highlight ... <position>", "(you can literally type anything between highlight and " +
                    "position. Position just has to be the last input)");
        }

        var pos = input[input.length - 1];

        ChessPosition position = ChessPosition.fromString(pos);
        if(position == null) {
            invalidInput(input, "highlight", "...", "<position>", "(invalid position: " + pos + ")");
            return;
        }

        var moves = user.getGame().validMoves(position);
        if(moves == null) {
            moves = new ArrayList<ChessMove>();
        }

        var highlight = new ArrayList<ChessPosition>(moves.size());
        for(var move: moves) {
            highlight.add(move.getEndPosition());
        }

        BoardPrinter.print(user.getGame().getBoard(), user.getPerspective(), highlight);
    }

    private void defaultInput(String[] inputs) {
        StringBuilder input = new StringBuilder();
        for(int i = 0; i < inputs.length - 1; i ++) {
            var text = inputs[i];
            input.append(text);
            input.append(" ");
        }
        if(inputs.length != 0) {
            input.append(inputs[inputs.length - 1]);
        }

        if(inputs.length > 1 && ! user.isObserver()) {
            System.out.println("Look, \"" + input + "\" is a bad input; however, I am a nice person and will try" +
                    " interpreting your garbage as a move.");

            String[] move = {inputs[inputs.length-2], inputs[inputs.length - 1]};

            makeMove(move);
        }
        else {
            invalidInput(inputs);
        }
    }
}
