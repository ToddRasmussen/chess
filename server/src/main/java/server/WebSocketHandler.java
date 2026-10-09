package server;

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
        gameSessions.computeIfAbsent(gameID, k -> new CopyOnWriteArrayList<>()).add(session);
        try {
            String player = service.getPlayer(command.getAuthToken());
            if (service.getTeam(gameID, player) != null) {
                broadcast(gameID, "Player " + player + " has Joined the Game");
            } else {
                broadcast(gameID, "Spectator has Started Watching the Game");
            }
        } catch (Exception e) {
            broadcast(gameID, "Someone has Joined the Game");
        }
    }

    private void handleMakeMove(Session session, UserGameCommand command) {
        //TODO
    }

    private void handleLeave(Session session, UserGameCommand command) {
        Integer gameID = command.getGameID();
        List<Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            sessions.remove(session);
        }
        try {
            String player = service.getPlayer(command.getAuthToken());
            if (service.getTeam(gameID, player) != null) {
                broadcast(gameID, "Player " + player + " has Left the Game");
            } else {
                broadcast(gameID, "Spectator has Stopped Watching the Game");
            }
        } catch (Exception e) {
            broadcast(gameID, "Someone has Left the Game");
        }
    }

    private void handleResign(Session session, UserGameCommand command) {
        //TODO
    }

    private void broadcast(Integer gameID, String message) {
        List<Session> sessions = gameSessions.get(gameID);
        if (sessions == null) return;
        String json = serializer.toJson(new ServerMessage(message));
        for (Session session : sessions) {
            try {
                session.getRemote().sendString(json);
            } catch (IOException e) {
                System.out.println("Failed to send message: " + e.getMessage());
            }
        }
    }
}