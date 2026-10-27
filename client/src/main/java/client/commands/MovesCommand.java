package client.commands;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;
import model.GameData;

import java.io.PrintStream;
import java.util.Collection;

public class MovesCommand extends Command {
    private final ChessGame.TeamColor team;
    private final ServerFacade server;
    private final Display display;

    /**
     * Command to display valid moves of a piece
     */
    public MovesCommand(ChessGame.TeamColor team, ServerFacade server, Display display) {
        super("moves <POSITION>", "list valid moves for piece at position");
        this.team = team;
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 2;
    }

    public State run(String[] input, PrintStream out) {
        try {
            ChessPosition pos = parsePosition(input[1]);
            if (pos == null) {
                out.println("Invalid position.");
                return null;
            }

            Integer gameID = server.getActiveGameID();
            if (gameID == null) {
                out.println("No active game found.");
                return null;
            }

            GameData gameData = server.game(gameID);
            Collection<ChessMove> validMoves = gameData.getGame().validMoves(pos);

            if (validMoves.isEmpty()) {
                out.println("No valid moves available for piece at " + input[1]);
            } else {
                out.print("Valid moves: ");
                display.highlight(validMoves.iterator().next().getStartPosition());
                StringBuilder sb = new StringBuilder();
                for (ChessMove move : validMoves) {
                    sb.append(move.getEndPosition().toString()).append(" ");
                    display.highlight(move.getEndPosition());
                }
                out.println(sb.toString().trim());
            }

        } catch (Exception e) {
            out.println("Unexpected Error: " + e.getMessage());
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