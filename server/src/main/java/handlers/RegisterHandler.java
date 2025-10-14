package handlers;

import io.javalin.http.Context;
import model.UserData;
import service.Service;

public class RegisterHandler extends Handler {
    public RegisterHandler(Context ctx, String username, String password, String email) {
        super(ctx);
        UserData user = new UserData(username, password, email);
        Service.createUser(user);
    }
}
