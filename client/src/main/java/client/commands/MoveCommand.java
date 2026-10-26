package client.commands;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;
import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;
import model.GameData;

import java.io.PrintStream;

public class MoveCommand extends Command {
    private final ChessGame.TeamColor team;
    private final Display display;
    private final ServerFacade server;

    /**
     * Command to move a piece on the board
     */
    public MoveCommand(ChessGame.TeamColor team, Display display, ServerFacade server) {
        super("move <START> <END> [PROMOTION]", "a piece");
        this.team = team;
        this.display = display;
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        if (inputs.length != 3 && inputs.length != 4) {
            return false;
        }

        try {
            ChessPosition start = parsePosition(inputs[1]);
            ChessPosition end = parsePosition(inputs[2]);
            if (start == null || end == null) {
                return false;
            }

            Integer gameID = server.getActiveGameID();
            if (gameID == null) {
                return false;
            }

            GameData gameData = server.game(gameID);
            ChessPiece piece = gameData.getGame().getBoard().getPiece(start);

            if (piece == null || piece.getTeamColor() != team) {
                return false;
            }

            boolean isPawn = (piece.getPieceType() == ChessPiece.PieceType.PAWN);
            boolean isPromotionRow = (team == ChessGame.TeamColor.WHITE && end.getRow() == 8) ||
                    (team == ChessGame.TeamColor.BLACK && end.getRow() == 1);

            boolean isPromotionMove = isPawn && isPromotionRow;

            if (inputs.length == 4) {
                return isPromotionMove;
            } else {
                return !isPromotionMove;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            ChessPosition start = parsePosition(inputs[1]);
            ChessPosition end = parsePosition(inputs[2]);

            ChessPiece.PieceType promotionPiece = null;
            if (inputs.length == 4) {
                promotionPiece = parsePromotion(inputs[3]);
            }

            ChessMove move = new ChessMove(start, end, promotionPiece);
            server.makeMove(move);
            display.triggerRedraw();
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

    private ChessPiece.PieceType parsePromotion(String promo) {
        return switch (promo.toUpperCase()) {
            case "Q", "QUEEN" -> ChessPiece.PieceType.QUEEN;
            case "R", "ROOK" -> ChessPiece.PieceType.ROOK;
            case "B", "BISHOP" -> ChessPiece.PieceType.BISHOP;
            case "N", "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
            default -> null;
        };
    }
}