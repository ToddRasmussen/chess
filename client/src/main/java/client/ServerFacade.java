package client;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ServerFacade {

    private URI databaseEndpoint;
    private URI userEndpoint;
    private URI sessionEndpoint;
    private URI gameEndpoint;
    private AuthData authorization;
    private HttpClient client;

    public ServerFacade(Integer port) {
        String baseURL = "https://localhost:" + port.toString() + "/";
        databaseEndpoint = URI.create(baseURL + "db");
        userEndpoint = URI.create(baseURL + "user");
        sessionEndpoint = URI.create(baseURL + "session");
        gameEndpoint = URI.create(baseURL + "game");
        client = HttpClient.newHttpClient();
    }

    private void addAuthorization(AuthData auth, HttpRequest.Builder builder) {
        builder.header("Authorization", auth.authToken());
    }

    public AuthData register(UserData user) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(userEndpoint);
        builder.POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(user)));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200 && response.statusCode() != 201) {
            throw new RuntimeException("Server gave error code: " + response.statusCode() + " with body: " + response.body());
        }
        authorization = new Gson().fromJson(response.body(), AuthData.class);
        return authorization;
    }

    public AuthData login(UserData user) {



        return authorization;
    }

    public void logout(AuthData auth) {

    }

    public void logout() {
        logout(authorization);
    }

    public Collection<GameData> games(AuthData auth) {

    }

    public Collection<GameData> games() {
        return games(authorization);
    }

    public int newGame(AuthData auth, String gameName) {

    }

    public int newGame(String gameName) {
        return newGame(authorization, gameName);
    }

    public void joinGame(AuthData auth, int gameID, String playerColor) {

    }

    public void joinGame(int gameID, String playerColor) {
        joinGame(authorization,gameID,playerColor);
    }

    public void reset() {

    }

}
