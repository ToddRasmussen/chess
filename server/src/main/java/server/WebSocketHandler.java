package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsMessageContext;
import org.eclipse.jetty.websocket.api.Session;
import service.Service;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class WebSocketHandler {

    private final Map<Integer, List<Session>> gameSessions = new ConcurrentHashMap<>();
    private final Gson serializer = new Gson();
    private final Service service;

    public WebSocketHandler(Service service) {
        this.service = service;
    }

    public void onConnect(WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("WebSocket connected: " + ctx.sessionId());
    }

    public void onMessage(WsMessageContext ctx) {
        UserGameCommand command = serializer.fromJson(ctx.message(), UserGameCommand.class);

        switch (command.getCommandType()) {
            case CONNECT -> handleConnect(ctx.session, command);
            case MAKE_MOVE -> handleMakeMove(ctx.session, command);
            case LEAVE -> handleLeave(ctx.session, command);
            case RESIGN -> handleResign(ctx.session, command);
        }
    }

    public void onClose(WsCloseContext ctx) {
        System.out.println("WebSocket closed: " + ctx.sessionId());
        for (List<Session> sessions : gameSessions.values()) {
            sessions.remove(ctx.session);
        }
    }

    private void handleConnect(Session session, UserGameCommand command) {
        Integer gameID = command.getGameID();
        try {
            String player = service.getPlayer(command.getAuthToken());
            if (player == null) {
                sendError(session, "Error: Unauthorized");
                return;
            }

            ChessGame game = service.getChessGame(gameID);
            if (game == null) {
                sendError(session, "Error: Invalid game ID");
                return;
            }

            gameSessions.computeIfAbsent(gameID, k -> new CopyOnWriteArrayList<>()).add(session);

            sendToOne(session, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game));

            if (service.getTeam(gameID, player) != null) {
                broadcast(gameID, session, new ServerMessage("Player " + player + " has Joined the Game"));
            } else {
                broadcast(gameID, session, new ServerMessage("Spectator has Started Watching the Game"));
            }
        } catch (Exception e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleMakeMove(Session session, UserGameCommand command) {
        Integer gameID = command.getGameID();
        try {
            String username = service.getPlayer(command.getAuthToken());
            ChessMove move = command.getMove();

            ChessGame updatedGame = service.makeMove(command.getAuthToken(), gameID, move);

            broadcastToAll(gameID, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, updatedGame));

            broadcast(gameID, session, new ServerMessage("Player " + username + " made move: " + move.toString()));

            if (updatedGame.getResults().isFinished()) {
                broadcastToAll(gameID, new ServerMessage("Game over! Result: " + updatedGame.getResults()));
            }

        } catch (Exception e) {
            sendError(session, e.getMessage());
        }
    }

    private void handleLeave(Session session, UserGameCommand command) {
        Integer gameID = command.getGameID();
        List<Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            sessions.remove(session);
        }
        try {
            String player = service.getPlayer(command.getAuthToken());

            service.leaveGame(command.getAuthToken(), gameID);

            if (service.getTeam(gameID, player) != null) {
                broadcastToAll(gameID, new ServerMessage("Player " + player + " has Left the Game"));
            } else {
                broadcastToAll(gameID, new ServerMessage("Spectator has Stopped Watching the Game"));
            }
        } catch (Exception e) {

        }
    }

    private void handleResign(Session session, UserGameCommand command) {
        Integer gameID = command.getGameID();
        try {
            String username = service.getPlayer(command.getAuthToken());
            ChessGame updatedGame = service.resignGame(command.getAuthToken(), gameID);

            broadcastToAll(gameID, new ServerMessage("Player " + username + " has Resigned the Game. Game Over!"));
        } catch (Exception e) {
            sendError(session, e.getMessage());
        }
    }

    private void sendToOne(Session session, ServerMessage msg) {
        try {
            session.getRemote().sendString(serializer.toJson(msg));
        } catch (IOException e) {
            System.out.println("Failed to send message: " + e.getMessage());
        }
    }

    private void sendError(Session session, String errorMsg) {
        try {
            ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMsg);
            session.getRemote().sendString(serializer.toJson(msg));
        } catch (IOException e) {
            System.out.println("Failed to send error: " + e.getMessage());
        }
    }

    private void broadcast(Integer gameID, Session excludeSession, ServerMessage message) {
        List<Session> sessions = gameSessions.get(gameID);
        if (sessions == null) return;
        String json = serializer.toJson(message);
        for (Session session : sessions) {
            if (session.equals(excludeSession)) continue;
            try {
                session.getRemote().sendString(json);
            } catch (IOException e) {
                System.out.println("Failed to send message: " + e.getMessage());
            }
        }
    }

    private void broadcastToAll(Integer gameID, ServerMessage message) {
        broadcast(gameID, null, message);
    }
}