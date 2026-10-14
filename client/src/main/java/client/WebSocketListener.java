package client;

import chess.ChessPosition;
import client.internal.ChessPositionDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import websocket.messages.ServerMessage;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;

public class WebSocketListener implements Listener {

    private WebSocket webSocket;
    private final Gson serializer;
    private final Consumer<String> notificationCallback;
    private final Consumer<String> errorCallback;
    private final Consumer<Integer> loadGameCallback;

    public WebSocketListener(URI endpoint,
                     Consumer<String> notificationCallback,
                     Consumer<String> errorCallback,
                     Consumer<Integer> loadGameCallback) {
        this.serializer = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(ChessPosition.class, new ChessPositionDeserializer())
                .create();
        this.notificationCallback = notificationCallback;
        this.errorCallback = errorCallback;
        this.loadGameCallback = loadGameCallback;

        HttpClient client = HttpClient.newHttpClient();
        this.webSocket = client.newWebSocketBuilder()
                .buildAsync(endpoint, this)
                .join();
    }

    public void sendCommand(Object command) {
        webSocket.sendText(serializer.toJson(command), true);
    }

    @Override
    public void onOpen(WebSocket webSocket) {
        WebSocket.Listener.super.onOpen(webSocket);
    }

    @Override
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        try {
            ServerMessage message = serializer.fromJson(data.toString(), ServerMessage.class);
            switch (message.getServerMessageType()) {
                case LOAD_GAME -> {
                    loadGameCallback.accept();
                }
                case NOTIFICATION -> {
                    notificationCallback.accept();
                }
                case ERROR -> {
                    errorCallback.accept();
                }
            }
        } catch (Exception e) {
            System.out.println("Error parsing socket message: " + e.getMessage());
        }
        return WebSocket.Listener.super.onText(webSocket, data, last);
    }

    public void close() {
        webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Closing connection");
    }
}