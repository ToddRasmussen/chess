package server;


import java.util.Collection;
import java.util.Map;

import dataaccess.ColorAlreadyTakenException;
import io.javalin.Javalin;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.AlreadyTakenException;
import service.DoesNotExistException;
import service.IncorrectPasswordException;
import service.InvalidAuthorizationException;
import service.Service;

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


    private void sendErrorMessage(Context ctx, Exception e, int code) {
        ctx.status(code).json(Map.of("message", "error: " + e.getMessage()));
    }

    private void sendSuccess(Context ctx, Object obj) {
        ctx.json(obj);
    }


    private String getAuth(Context ctx) {
        return ctx.header("authToken");
    }

    private UserData getUser(Context ctx) {
        return ctx.bodyAsClass(UserData.class);
    }

    private void clearApplication(Context ctx) {
        service.reset();
    }


    private void registerUser(Context ctx) {
        try {
            UserData user = getUser(ctx);
            AuthData auth = service.registerUser(user);
            sendSuccess(ctx, auth);
        } catch (AlreadyTakenException e) {
            sendErrorMessage(ctx, e, 403);
        } catch (Exception e){
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void loginUser(Context ctx) {
        try {
            UserData user = getUser(ctx);
            AuthData auth = service.loginUser(user);
            sendSuccess(ctx, auth);
        } catch (DoesNotExistException | IncorrectPasswordException e) {
            sendErrorMessage(ctx, e, 403);
        } catch (Exception e){
            sendErrorMessage(ctx, e, 500);
        }
    }


    private void logoutUser(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            service.logoutUser(authToken);
            sendSuccess(ctx, null);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (Exception e){
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void listGames(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            Collection<GameData> games = service.listGames(authToken);
            sendSuccess(ctx, games);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (Exception e){
            sendErrorMessage(ctx, e, 500);
        }
    }


    private void createGame(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String gameName = (String) body.get("gameName");
            String gameID = service.createGame(authToken, gameName);
            sendSuccess(ctx, gameID);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (Exception e){
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void joinGame(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String playerColor = (String) body.get("playerColor");
            String gameID = (String) body.get("gameID");
            service.joinGame(authToken, gameID, playerColor);
            sendSuccess(ctx, null);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (ColorAlreadyTakenException e) {
            sendErrorMessage(ctx, e, 403);
        } catch (Exception e){
            sendErrorMessage(ctx, e, 500);
        }
    }

}
