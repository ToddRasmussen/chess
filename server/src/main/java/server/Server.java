package server;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import com.google.gson.Gson;
import dataaccess.ColorAlreadyTakenException;
import dataaccess.DataAccessException;
import io.javalin.Javalin;
import io.javalin.http.Context;

import model.AuthData;
import model.GameData;
import model.UserData;
import dataaccess.UnknownColorException;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.DoesNotExistException;
import service.IncorrectPasswordException;
import service.InvalidAuthorizationException;
import service.Service;

public class Server {

    private final Javalin javalin;
    private final Gson serializer = new Gson();
    private Service service;

    public Server() {
        service = new Service();

        javalin = Javalin.create(config -> config.staticFiles.add("web"));
        javalin.delete("/db", this::clearApplication);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this::logoutUser);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void sendErrorMessage(Context ctx, Exception e, int code) {
        ctx.status(code);
        ctx.contentType("application/json");
        ctx.result(serializer.toJson(Map.of("message", "Error:" + e.getMessage())));
    }

    private void sendSuccess(Context ctx, Object obj) {
        ctx.status(200);
        ctx.contentType("application/json");
        if (obj != null) {
            ctx.result(serializer.toJson(obj));
        } else {
            ctx.result("{}");
        }
    }

    private String getAuth(Context ctx) {
        return ctx.header("authorization");
    }

    private UserData getUser(Context ctx) {
        return serializer.fromJson(ctx.body(), UserData.class);
    }

    private void clearApplication(Context ctx) {
        try {
            service.reset();
            sendSuccess(ctx, null);
        } catch (Exception e) {
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void registerUser(Context ctx) {
        try {
            UserData user = getUser(ctx);
            AuthData auth = service.registerUser(user);
            sendSuccess(ctx, auth);
        } catch (BadRequestException e) {
            sendErrorMessage(ctx, e, 400);
        } catch (AlreadyTakenException e) {
            sendErrorMessage(ctx, e, 403);
        } catch (Exception e) {
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void loginUser(Context ctx) {
        try {
            UserData user = getUser(ctx);
            AuthData auth = service.loginUser(user);
            sendSuccess(ctx, auth);
        } catch (BadRequestException e) {
            sendErrorMessage(ctx, e, 400);
        } catch (DoesNotExistException | IncorrectPasswordException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (Exception e) {
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
        } catch (Exception e) {
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void listGames(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            Collection<GameData> games = service.listGames(authToken);
            sendSuccess(ctx, Map.of("games", games));
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (Exception e) {
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void createGame(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            Map<String, Object> body = serializer.fromJson(ctx.body(), Map.class);
            String gameName = (body != null) ? (String) body.get("gameName") : null;

            Object gameID = service.createGame(authToken, gameName);
            sendSuccess(ctx, Map.of("gameID", gameID));
        } catch (BadRequestException e) {
            sendErrorMessage(ctx, e, 400);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (Exception e) {
            sendErrorMessage(ctx, e, 500);
        }
    }

    private void joinGame(Context ctx) {
        try {
            String authToken = getAuth(ctx);
            Map<?, ?> body = serializer.fromJson(ctx.body(), Map.class);
            String playerColor = null;
            int gameID = 0;
            if (body != null) {
                playerColor = (String) body.get("playerColor");
                Object raw = body.get("gameID");
                if (raw != null) {
                    gameID = (int) (double) raw;
                }
            }
            service.joinGame(authToken, gameID, playerColor);
            sendSuccess(ctx, null);
        } catch (BadRequestException | UnknownColorException | DataAccessException e) {
            sendErrorMessage(ctx, e, 400);
        } catch (InvalidAuthorizationException e) {
            sendErrorMessage(ctx, e, 401);
        } catch (ColorAlreadyTakenException e) {
            sendErrorMessage(ctx, e, 403);
        } catch (Exception e) {
            sendErrorMessage(ctx, e, 500);
        }
    }
}