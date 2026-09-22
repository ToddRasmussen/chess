package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class ServerFacade {

    private final URI databaseEndpoint;
    private final URI userEndpoint;
    private final URI sessionEndpoint;
    private final URI gameEndpoint;
    private AuthData authorization;
    private final HttpClient client;
    private final Gson serializer;

    public ServerFacade(Integer port) {
        String baseURL = "http://localhost:" + port.toString() + "/";
        databaseEndpoint = URI.create(baseURL + "db");
        userEndpoint = URI.create(baseURL + "user");
        sessionEndpoint = URI.create(baseURL + "session");
        gameEndpoint = URI.create(baseURL + "game");
        client = HttpClient.newHttpClient();
        authorization = null;
        serializer = new Gson();
    }

    private void addAuthorization(AuthData auth, HttpRequest.Builder builder) throws Exception {
        if (auth == null || auth.authToken() == null) {
            throw new Exception("No Authorization Given");
        }
        builder.header("Authorization", auth.authToken());
    }

    private void handleStatusCode(int statusCode) throws Exception {
        switch (statusCode) {
            case 200: return;
            case 400: throw new Exception("Bad Request");
            case 401: throw new Exception("Unauthorized");
            case 403: throw new Exception("Already Taken");
            default: throw new Exception("Server Error");
        }
    }


    private AuthData authenticate(UserData user, HttpRequest.Builder builder ) throws Exception {
        builder.POST(HttpRequest.BodyPublishers.ofString(serializer.toJson(user)));
        builder.header("Content-Type", "application/json");
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
        authorization = serializer.fromJson(response.body(), AuthData.class);
        return authorization;
    }


    public AuthData register(UserData user) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(userEndpoint);
        return authenticate(user, builder);
    }

    public AuthData login(UserData user) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.uri(sessionEndpoint);
        return authenticate(user, builder);
    }

    public void logout(AuthData auth) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(sessionEndpoint);
        builder.DELETE();
        addAuthorization(auth, builder);
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
    }

    public void logout() throws Exception {
        logout(authorization);
        authorization = null;
    }

    public Collection<GameData> games(AuthData auth) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(gameEndpoint);
        addAuthorization(auth, builder);
        builder.GET();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
        Collection<GameData> listGames = new LinkedList<>();
        // for each game
        Map<?, ?> body = serializer.fromJson(response.body(), Map.class);
        List<?> games = serializer.fromJson(body.get("games"), List.class);
        for (Object gameStr : games){
            Map<?, ?> rawGame = serializer.fromJson(gameStr, Map.class);
            ChessBoard board = new ChessBoard();
            // for each piece
            {
                ChessGame.TeamColor pieceColor = ChessGame.TeamColor.valueOf(colorStr);
                ChessPiece.PieceType type = ChessPiece.PieceType.valueOf(typeStr);
                ChessPiece piece = new ChessPiece(pieceColor, type);
                ChessPosition position = new ChessPosition(Integer.valueOf(rowStr), Integer.valueOf(colStr));
                board.addPiece(position, piece);
            }
            ChessGame game = new ChessGame();
            game.setBoard(board);
            GameData gameData = new GameData(rawGame.get("GameID"), rawGame.get("gameName"), game);
            listGames.add(gameData);
        }
        return listGames;
    }

    public Collection<GameData> games() throws Exception {
        return games(authorization);
    }

    private record NewGameRequest(String gameName) {}
    private record NewGameResponse(int gameID) {}

    public int newGame(AuthData auth, String gameName) throws Exception {
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

    public int newGame(String gameName) throws Exception {
        return newGame(authorization, gameName);
    }

    private record JoinGameRequest(int gameID, String playerColor) {}

    public void joinGame(AuthData auth, int gameID, String playerColor) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(gameEndpoint);
        addAuthorization(auth, builder);
        String json = serializer.toJson(new JoinGameRequest(gameID, playerColor));
        builder.PUT(HttpRequest.BodyPublishers.ofString(json));
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
    }

    public void joinGame(int gameID, String playerColor) throws Exception {
        joinGame(authorization,gameID,playerColor);
    }

    public void reset() throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder();
        builder.header("Content-Type", "application/json");
        builder.uri(databaseEndpoint);
        builder.DELETE();
        HttpResponse<String> response = client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
        handleStatusCode(response.statusCode());
    }

}
