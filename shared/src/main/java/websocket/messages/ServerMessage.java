package websocket.messages;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * <p>
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    private String message;
    private Integer gameID;
    private Exception exception;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }
    public ServerMessage(String message) {
        this.message = message;
        this.serverMessageType = ServerMessageType.NOTIFICATION;
    }
    public ServerMessage(Integer gameID) {
        this.gameID = gameID;
        this.serverMessageType = ServerMessageType.LOAD_GAME;
    }
    public ServerMessage(Exception e) {
        this.exception = e;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    public String getMessage() {
        return message;
    }
    public Integer getGameID() {
        return gameID;
    }
    public Exception getException() {
        return exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage that)) {
            return false;
        }
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
