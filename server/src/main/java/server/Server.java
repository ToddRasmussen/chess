package server;


import java.util.Map;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {

        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);

        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void Error(Context ctx, int code, String Message) {
        ctx.status(code).json(Map.of("message", Message));
    }

    private void Respond(Context ctx, int code, Object data) {
        ctx.status(code).json(data);
    }

    private record RegisterRequest(String username, String password, String email) {}
    private record LoginRequest(String username, String password) {}


    private record LoginResult(String username, String authToken) {}



    private void registerUser(Context ctx) {

        RegisterRequest req = ctx.bodyAsClass(RegisterRequest.class);
        String username = req.username();
        String password = req.password();
        String email = req.email();

        for (String credential : new String[]{username, password, email}) {
            if (credential == null || credential == "") {
                Error(ctx, 400, "Error: bad request");
                return;
            }
        }
        if (password.length() < 8) {
            Error(ctx,500, "Error: Password must be at least 8 long");
            return;
        }

        //TODO: Logic to reject if username or email already in use

        //TODO: Logic to add to database

        //TODO: Logic to get AuthToken
        String authToken;

        Respond(ctx, 200, new RegisterResult(username, authToken));
    }
    
    
    private void loginUser(Context ctx) {

        LoginRequest req = ctx.bodyAsClass(LoginRequest.class);
        String username = req.username();
        String password = req.password();

        for (String credential : new String[]{username, password}) {
            if (credential == null || credential == "") {
                Error(ctx, 400, "Error: bad request");
                return;
            }
        }

        //TODO: Logic to check if its a known username

        //TODO: Logic to check if correct password for username

        //TODO: Logic to get AuthToken
        String authToken

        Respond(ctx, 200, new LoginResult(username, authToken));
    }

}
