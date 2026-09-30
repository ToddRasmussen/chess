package client;

import chess.ChessPosition;
import com.google.gson.*;
import java.lang.reflect.Type;

public class ChessPositionDeserializer implements JsonDeserializer<ChessPosition> {
    @Override
    public ChessPosition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String pos = json.getAsString();
        char colChar = pos.charAt(0);
        int col = colChar - 'a' + 1;
        int row = Character.getNumericValue(pos.charAt(1));
        return new ChessPosition(row, col);
    }
}