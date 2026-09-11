package server;


import java.util.Map;
import java.util.Collection;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;
    private Service service;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::clearApplication);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this:: logoutUser);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);


        service = new Service();
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }


    private void sendErrorMessage(Exception e, int code) {
        ctx.status(code).json(Map.of("message", "error: " + e.getMessage()));
    }

    private void clearApplication(Context ctx) {

    }


    private void registerUser(Context ctx) {
        try {
            service.registerUser(ctx.bodyAsClass(UserData.class));
            //TODO
        } catch (AlreadyTakenException e) {
            sendErrorMessage(e, 403);
        }
    }

    private void loginUser(Context ctx) {
        try {
            service.loginUser(ctx.bodyAsClass(UserData.class));
            //TODO
        } catch (DoesNotExistException | IncorrectPasswordException) {
            sendErrorMessage(e, 403);
        }
    }


    private void logoutUser(Context ctx) {
        try {
            service.logoutUser(/*TODO*/);
            //TODO
        } catch (InvalidAuthorizationException) {
            sendErrorMessage(e, 401);
        }
    }

    private void listGames(Context ctx) {
        try {
            service.listGames(/*TODO*/);
            //TODO
        } catch (InvalidAuthorizationException) {
            sendErrorMessage(e, 401);
        }
    }


    private void createGame(Context ctx) {
        try {
            service.createGame(/*TODO*/);
            //TODO
        } catch (InvalidAuthorizationException) {
            sendErrorMessage(e, 401);
        }
    }

    private void joinGame(Context ctx) {
        try {
            service.joinGame(/*TODO*/);
            //TODO
        } catch (InvalidAuthorizationException) {
            sendErrorMessage(e, 401);
        }
    }

}
