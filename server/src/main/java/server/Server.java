package server;


import java.util.Map;
import java.util.Collection;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {

        javalin.delete("/db", this::clearApplication);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this:: logoutUser);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);

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
    private record NewGameRequest(String gameName) {}
    private record JoinGameRequest(String playerColor, int gameID) {}


    private record LoginResult(String username, String authToken) {}
    private record gameResult(int gameID, String whiteUsername, String blackUsername, String gameName) {}
    private record gameListResult(Collection<gameResult> games) {}
    


    private void clearApplication(Context ctx) {

        //TODO

        Respond(ctx, 200, null);
    }


    private void registerUser(Context ctx) {

        RegisterRequest req = ctx.bodyAsClass(RegisterRequest.class);
        String username = req.username();
        String password = req.password();
        String email = req.email();

        for (String credential : new String[]{username, password, email}) {
            if (credential == null || credential.isEmpty()) {
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
            if (credential == null || credential.isEmpty()) {
                Error(ctx, 400, "Error: bad request");
                return;
            }
        }

        //TODO: Logic to check if correct password for username

        //TODO: Logic to get AuthToken
        String authToken

        Respond(ctx, 200, new LoginResult(username, authToken));
    }



    private String checkAuthentication(Context ctx) {
        String authToken = ctx.header("authorization");
        //TODO: Logic to check if AuthToken is valid (if not return null)

        //TODO: Logic to get username of authToken
    }

    private void logoutUser(Context ctx) {
        String username = checkAuthentication(ctx);
        if (username == null) {
            Error(ctx, 401, "Error: unauthorized");
        }

        //TODO: Logic to deactivate authToken
    }


    private void listGames(Context ctx) {
        String username = checkAuthentication(ctx);
        if (username == null) {
            Error(ctx, 401, "Error: unauthorized");
        }

        //TODO: Fetch list of games
        Respond(ctx, 200, new gameListResult(gameList));
    }


    private void createGame(Context ctx) {
        String username = checkAuthentication(ctx);
        if (username == null) {
            Error(ctx, 401, "Error: unauthorized");
        }
        NewGameRequest req = ctx.bodyAsClass(NewGameRequest.class);
        String gameName = req.gameName();

        //TODO: Logic to create game

        Respond(ctx, 200, new gameResult(gameID, "", "", gameName));
    }

    private void joinGame(Context ctx) {
        String username = checkAuthentication(ctx);
        if (username == null) {
            Error(ctx, 401, "Error: unauthorized");
        }
        JoinGameRequest req = ctx.bodyAsClass(JoinGameRequest.class);
        String playerColor = req.playerColor();
        int gameID = req.gameID();

        //TODO: Logic to add username as game color


        //TODO: Logic to get game info

        Respond(ctx, 200, new gameResult(gameID, whiteUsername, blackUsername, gameName));
    }

}
