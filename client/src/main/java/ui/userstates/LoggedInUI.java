package ui.userstates;

import chess.ChessGame;
import model.AuthData;
import model.User;
import server.ServerFacade;
import ui.BoardPrinter;
import ui.EscapeSequences;
import websocket.commands.UserGameCommand;

public class LoggedInUI extends CLIUserInterface {
    private final String expectedPlay = "play <game id number> <player piece color>";
    private final String expectedObserve = "observe <game id number>";

    public LoggedInUI(User user, ServerFacade serverFacade) {
        super(user, serverFacade);
    }

    public CLIUserInterface handleInput(String[] inputs) {
        CLIUserInterface ui = this;

        switch (inputs[0].toLowerCase()) {
            case "help" -> help();
            case "logout" -> {
                logout();
                ui = new LoggedOutUI(user, serverFacade);
            }
            case "create" -> createGame(inputs);
            case "list" -> listGames();
            case "play", "join" -> ui = joinGame(inputs);
            case "observe" -> ui = observeGame(inputs);
            default -> invalidInput(inputs);
        }

        return ui;
    }

    protected void help() {
        super.help();
        String[] helpStrings = {
                EscapeSequences.SET_TEXT_COLOR_GREEN + "logout" + EscapeSequences.RESET_TEXT_COLOR
                        + " - logs you out",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "create <game name>" + EscapeSequences.RESET_TEXT_COLOR
                        + " - creates a chess game of the specified name",

                EscapeSequences.SET_TEXT_COLOR_GREEN + "list" + EscapeSequences.RESET_TEXT_COLOR
                        + " - lists all games currently available on the server",

                EscapeSequences.SET_TEXT_COLOR_GREEN + expectedPlay + EscapeSequences.RESET_TEXT_COLOR
                        + " - play the specified game with the specified piece color",

                EscapeSequences.SET_TEXT_COLOR_GREEN + expectedObserve + EscapeSequences.RESET_TEXT_COLOR
                        + " - watch the specified game"
        };

        for (var string : helpStrings) {
            System.out.println(string);
        }
    }

    private void createGame(String[] inputs) {
        if (inputs.length < 2) {
            invalidInput(inputs, "create <game name>");
            return;
        }

        var name = new StringBuilder(inputs[1]);
        for (int i = 2; i < inputs.length; i++) {
            name.append(" ");
            name.append(inputs[i]);
        }

        serverFacade.createGame(user.getAuthToken(), name.toString());
    }

    private CLIUserInterface joinGame(String[] inputs) {
        try {
            var gameID = validateGameIDInput(inputs, expectedPlay);
            var team = validateTeamColor(inputs);
            var response = serverFacade.joinGame(user.getAuthToken(), gameID, team);
            System.out.println(response.message());

            if(response.joinedGame()) {
                BoardPrinter.print(response.game().getBoard(), team);
                user.setGame(response.game());
                user.setPerspective(team);
                user.setObserver(false);

                var joinType = team == ChessGame.TeamColor.WHITE ? UserGameCommand.JoinType.WHITE : UserGameCommand.JoinType.BLACK;
                var joinCommand = new UserGameCommand(user.getAuthToken(), response.id(), joinType);
                return new GameplayUI(user, serverFacade, joinCommand);
            }
            return this;
        } catch (RuntimeException e) {
            return this;
        }
    }

    private CLIUserInterface observeGame(String[] inputs) {

        try {
            var gameID = validateGameIDInput(inputs, expectedObserve);
            var response = serverFacade.observeGame(user.getAuthToken(), gameID);

            System.out.println(response.message());

            if(response.joinedGame()) {
                BoardPrinter.print(response.game().getBoard(), ChessGame.TeamColor.WHITE);
                user.setGame(response.game());
                user.setPerspective(ChessGame.TeamColor.WHITE);
                user.setObserver(true);
                var connect = new UserGameCommand(user.getAuthToken(), response.id(), UserGameCommand.JoinType.OBSERVER);
                return new GameplayUI(user, serverFacade, connect);
            }

            return this;
        } catch (RuntimeException e) {
            return this;
        }
    }

    private ChessGame.TeamColor validateTeamColor(String[] inputs) {
        if (inputs.length < 3) {
            invalidInput(inputs, expectedPlay);
        }

        var color = inputs[2];
        if (color.equalsIgnoreCase("white")) {
            return ChessGame.TeamColor.WHITE;
        } else if (color.equalsIgnoreCase("black")) {
            return ChessGame.TeamColor.BLACK;
        } else {
            invalidInput(inputs, expectedPlay);
            System.out.println("Player piece color must be either \"black\" or \"white\". Case doesn't matter.");
            throw new RuntimeException();
        }
    }

    private int validateGameIDInput(String[] inputs, String expected) {
        if (inputs.length < 2) {
            invalidInput(inputs, expected);
        }
        try {
            return Integer.parseInt(inputs[1]);
        } catch (NumberFormatException e) {
            invalidInput(inputs, expected);
            System.out.println("(game id number must be a number like \"1\" or \"7\" but not" +
                    "spelled out like \"nine\"");
            throw new RuntimeException();
        }
    }

    private void listGames() {
        serverFacade.listGames(user.getAuthToken());
    }

    private void logout() {
        serverFacade.logout(new AuthData(user.getUsername(), user.getAuthToken()));
        user.setAuthToken(null);
        user.setUsername(null);
        user.setLoggedIn(false);
    }
}
