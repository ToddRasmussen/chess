package server;


import java.util.Map;
import java.util.Collection;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::clearApplication);
        javalin.post("/user", this::registerUser);
        javalin.post("/session", this::loginUser);
        javalin.delete("/session", this:: logoutUser);
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

    private void clearApplication(Context ctx) {

    }


    private void registerUser(Context ctx) {

    }
    
    
    private void loginUser(Context ctx) {

    }


    private void logoutUser(Context ctx) {

    }

    private void listGames(Context ctx) {

    }


    private void createGame(Context ctx) {

    }

    private void joinGame(Context ctx) {

    }

}
