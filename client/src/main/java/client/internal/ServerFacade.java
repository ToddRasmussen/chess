package client.internal;

import chess.ChessMove;
import chess.ChessPosition;
import client.exceptions.AlreadyTakenException;
import client.exceptions.BadRequestException;
import client.exceptions.ServerException;
import client.exceptions.UnauthorizedException;
import com.google.gson.GsonBuilder;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

public class ServerFacade {

    private final URI databaseEndpoint;
    private final URI userEndpoint;
    private final URI sessionEndpoint;
    private final URI gameEndpoint;
    private final URI wsEndpoint;
    private AuthData authorization;
    private final HttpClient client;
    private final Gson serializer;
    private Consumer<String> notificationCallback;
    private Consumer<Exception> errorCallback;
    private Consumer<Integer> loadGameCallback;
    private Integer activeGameID;
    private WebSocketListener webSocket;

    public ServerFacade(Integer port) {
        String baseURL = "http://localhost:" + port.toString() + "/";
        databaseEndpoint = URI.create(baseURL + "db");
        userEndpoint = URI.create(baseURL + "user");
        sessionEndpoint = URI.create(baseURL + "session");
        gameEndpoint = URI.create(baseURL + "game");
        wsEndpoint = URI.create(baseURL + "ws");
        client = HttpClient.newHttpClient();
        authorization = null;
        serializer = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(ChessPosition.class, new ChessPositionDeserializer())
                .create();
        loadGameCallback = null;
        errorCallback = null;
        notificationCallback = null;
    }

    public ServerFacade(Integer port, Consumer<String> notificationCallback, Consumer<Exception> errorCallback, Consumer<Integer> loadGameCallback) {
        this(port);
        this.notificationCallback = notificationCallback;
        this.errorCallback = errorCallback;
        this.loadGameCallback = loadGameCallback;
    }

    private void addAuthorization(AuthData auth, HttpRequest.Builder builder) throws UnauthorizedException {
        if (auth == null || auth.authToken() == null) {
            throw new UnauthorizedException("No Authorization Given");
        }
        builder.header("Authorization", auth.authToken());
    }

    private void handleStatusCode(int statusCode) throws BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        switch (statusCode) {
            case 200: return;
            case 400: throw new BadRequestException("Bad Request");
            case 401: throw new UnauthorizedException("Unauthorized");
            case 403: throw new AlreadyTakenException("Already Taken");
            default: throw new ServerException("Server Error");
        }
    }


    private AuthData authenticate(UserData user, HttpRequest.Builder builder)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        builder.POST(HttpRequest.BodyPublishers.ofString(serializer.toJson(user)));
        builder.header("Content-Type", "application/json");
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
        authorization = serializer.fromJson(response.body(), AuthData.class);
        return authorization;
    }


    public AuthData register(UserData user)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(userEndpoint);
        return authenticate(user, builder);
    }

    public AuthData login(UserData user)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException  {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(sessionEndpoint);
        return authenticate(user, builder);
    }

    public void logout(AuthData auth)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(sessionEndpoint);
        builder.DELETE();
        addAuthorization(auth, builder);
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
    }

    public void logout()
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        logout(authorization);
        authorization = null;
    }

    private record GamesListResponse (Collection<GameData> games) {}

    public Collection<GameData> games(AuthData auth)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(gameEndpoint);
        addAuthorization(auth, builder);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
        GamesListResponse result = serializer.fromJson(response.body(), GamesListResponse.class);
        return (result != null && result.games() != null) ? result.games() : new LinkedList<>();
    }

    public Collection<GameData> games()
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        return games(authorization);
    }


    public GameData game(Integer gameID, AuthData auth)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        for (GameData game : games(auth)) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        throw new BadRequestException("Invalid GameID");
    }

    public GameData game(Integer gameID)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        return game(gameID, authorization);
    }

    public GameData game(AuthData auth)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        return game(activeGameID, auth);
    }

    public GameData game()
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        return game(authorization);
    }

    private record NewGameRequest(String gameName) {}
    private record NewGameResponse(int gameID) {}

    public int newGame(AuthData auth, String gameName)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(gameEndpoint);
        addAuthorization(auth, builder);
        String json = serializer.toJson(new NewGameRequest(gameName));
        builder.POST(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
        return serializer.fromJson(response.body(), NewGameResponse.class).gameID();
    }

    public int newGame(String gameName)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        return newGame(authorization, gameName);
    }

    private record JoinGameRequest(int gameID, String playerColor) {}

    public void joinGame(AuthData auth, int gameID, String playerColor)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(gameEndpoint);
        addAuthorization(auth, builder);
        String json = serializer.toJson(new JoinGameRequest(gameID, playerColor));
        builder.PUT(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
        this.activeGameID = gameID;
        // Initialize WebSocket connection if not already open
        if (webSocket == null && loadGameCallback != null) {
            try {
                webSocket = new WebSocketListener(
                        wsEndpoint,
                        notificationCallback,
                        errorCallback,
                        loadGameCallback
                );
                // Send initial CONNECT command after connection opening
                UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth.authToken(), gameID);
                webSocket.sendCommand(connectCommand);
            } catch (Exception e) {
                throw new ServerException("Failed to establish WebSocket connection: " + e.getMessage());
            }
        }
    }

    public void joinGame(int gameID, String playerColor)
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        joinGame(authorization,gameID,playerColor);
    }

    public void reset()
            throws InterruptedException, IOException, BadRequestException, UnauthorizedException, AlreadyTakenException, ServerException {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(databaseEndpoint);
        builder.DELETE();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
    }


    public void makeMove(AuthData auth, ChessMove move) {
        if (webSocket != null && activeGameID != null) {
            UserGameCommand command = new UserGameCommand(move, auth.authToken(), activeGameID);
            webSocket.sendCommand(command);
        }
    }

    public void makeMove(ChessMove move) {
        makeMove(authorization, move);
    }

    public void resign(AuthData auth) {
        if (webSocket != null) {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, auth.authToken(), activeGameID);
            webSocket.sendCommand(command);
        }
    }

    public void resign() {
        resign(authorization);
    }


    public Integer getActiveGameID() {
        return activeGameID;
    }

    public void leave(AuthData auth) {
        if (webSocket != null) {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authorization.authToken(), activeGameID);
            webSocket.sendCommand(command);
            webSocket.close();
            webSocket = null;
            activeGameID = null;
        }
    }

    public void leave() {
        leave(authorization);
    }
}
