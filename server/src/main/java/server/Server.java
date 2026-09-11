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

    private void sendSuccess(Object obj) {
        ctx.status(200).json(Map.of(obj));
    }

    private String getAuth(Context ctx) {
        //TODO
    }

    private UserData getUser(Context ctx) {
        return ctx.bodyAsClass(UserData.class);;
    }

    private void clearApplication(Context ctx) {
        //TODO
    }


    private void registerUser(Context ctx) {
        try {
            UserData user = getUser(ctx);
            AuthData auth = service.registerUser(user);
            sendSuccess(auth);
        } catch (AlreadyTakenException e) {
            sendErrorMessage(e, 403);
        } catch (Exception e){
            sendErrorMessage(e, 500);
        }
    }

    private void loginUser(Context ctx) {
        try {
            UserData user = getUser(ctx);
            AuthData auth = service.loginUser(user);
            sendSuccess(auth);
        } catch (DoesNotExistException | IncorrectPasswordException e) {
            sendErrorMessage(e, 403);
        } catch (Exception e){
            sendErrorMessage(e, 500);
        }
    }


    private void logoutUser(Context ctx) {
        try {
            service.logoutUser(/*TODO*/);
            sendSuccess(null);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(e, 401);
        } catch (Exception e){
            sendErrorMessage(e, 500);
        }
    }

    private void listGames(Context ctx) {
        try {
            Collection<GameData> games = service.listGames(/*TODO*/);
            sendSuccess(games);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(e, 401);
        } catch (Exception e){
            sendErrorMessage(e, 500);
        }
    }


    private void createGame(Context ctx) {
        try {
            String gameID = service.createGame(/*TODO*/);
            sendSuccess(gameID);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(e, 401);
        } catch (Exception e){
            sendErrorMessage(e, 500);
        }
    }

    private void joinGame(Context ctx) {
        try {
            service.joinGame(/*TODO*/);
            //TODO
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(e, 401);
        } catch (ColorAlreadyTakenException e) {
            sendErrorMessage(e, 403)
        } catch (Exception e){
            sendErrorMessage(e, 500);
        }
    }

}
