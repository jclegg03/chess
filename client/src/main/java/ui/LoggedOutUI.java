package ui;

import model.User;
import model.UserData;
import server.ServerFacade;

public class LoggedOutUI extends CLIUserInterface {
    private final String[] expectedLogin = {"login", "<username>", "<password>"};
    private final String[] expectedRegister = {"register", "<username>", "<password>", "<email>"};

    public LoggedOutUI(User user, ServerFacade serverFacade) {
        super(user, serverFacade);
    }

    public CLIUserInterface handleInput(String[] inputs) {
        CLIUserInterface ui = this;

        switch (inputs[0].toLowerCase()) {
            case "help" -> printHelpLoggedOut();
            case "login" -> {
                if (inputs.length != expectedLogin.length) {
                    invalidInput(inputs, expectedLogin);
                    break;
                }
                login(inputs[1], inputs[2]);
                ui = new LoggedInUI(user, serverFacade);
            }
            case "register" -> {
                if (inputs.length != expectedRegister.length) {
                    invalidInput(inputs, expectedRegister);
                    break;
                }
                register(inputs[1], inputs[2], inputs[3]);
                ui = new LoggedInUI(user, serverFacade);
            }
            default -> invalidInput(inputs);
        }

        return ui;
    }

    private void register(String username, String password, String email) {
        try {
            user.setAuthToken(serverFacade.register(new UserData(username, password, email)));
            user.setUsername(username);
            user.setLoggedIn(true);
        } catch (RuntimeException e) {
        }
    }

    private void login(String username, String password) {
        try {
            user.setAuthToken(serverFacade.login(new UserData(username, password, null)));
            user.setUsername(username);
            user.setLoggedIn(true);
        } catch (RuntimeException e) {
        }
    }

    private void printHelpLoggedOut() {
        printDefaultHelp();
        var loginString = new StringBuilder(EscapeSequences.SET_TEXT_COLOR_GREEN);
        for (String item : expectedLogin) {
            loginString.append(item);
            loginString.append(" ");
        }
        loginString.append(EscapeSequences.RESET_TEXT_COLOR);
        loginString.append("- to gain access to our premium chess servers and games");
        System.out.println(loginString);

        var registerString = new StringBuilder(EscapeSequences.SET_TEXT_COLOR_GREEN);
        for (String item : expectedRegister) {
            registerString.append(item).append(" ");
        }
        registerString.append(EscapeSequences.RESET_TEXT_COLOR);
        registerString.append("- to create a user and login to our great server");
        System.out.println(registerString);
    }
}
