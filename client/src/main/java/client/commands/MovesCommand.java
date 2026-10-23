package client.commands;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.Display;
import client.ServerFacade;
import client.internal.State;
import model.GameData;

import java.util.Collection;

public class MovesCommand extends Command {
    private final ChessGame.TeamColor team;
    private final Display display;
    private final ServerFacade server;

    /**
     * Command to display valid moves of a piece
     */
    public MovesCommand(ChessGame.TeamColor team, Display display, ServerFacade server) {
        super("moves <POSITION>", "list valid moves for piece at position");
        this.team = team;
        this.display = display;
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 2;
    }

    public State run(String[] inputs) {
        try {
            ChessPosition pos = parsePosition(inputs[1]);
            if (pos == null) {
                System.out.println("Invalid position.");
                return null;
            }

            Integer gameID = server.getActiveGameID();
            if (gameID == null) {
                System.out.println("No active game found.");
                return null;
            }

            GameData gameData = server.game(gameID);
            Collection<ChessMove> validMoves = gameData.getGame().validMoves(pos);

            if (validMoves.isEmpty()) {
                System.out.println("No valid moves available for piece at " + inputs[1]);
            } else {
                System.out.print("Valid moves: ");
                StringBuilder sb = new StringBuilder();
                for (ChessMove move : validMoves) {
                    sb.append(move.getEndPosition().toString()).append(" ");
                }
                System.out.println(sb.toString().trim());
            }

            display.triggerRedraw();
        } catch (Exception e) {
            System.out.println("Unexpected Error: " + e.getMessage());
        }
        return null;
    }

    private ChessPosition parsePosition(String posStr) {
        if (posStr == null || posStr.length() < 2) {
            return null;
        }
        char colChar = Character.toLowerCase(posStr.charAt(0));
        int col = colChar - 'a' + 1;
        int row;
        try {
            row = Character.getNumericValue(posStr.charAt(1));
        } catch (Exception e) {
            return null;
        }
        ChessPosition pos = new ChessPosition(row, col);
        return pos.isInBounds() ? pos : null;
    }
}