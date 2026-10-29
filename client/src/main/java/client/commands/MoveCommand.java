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

    public boolean isCommand(String input) {
        try {
            return super.isCommand(input) && !server.game().getGame().getResults().isFinished();
        } catch (Exception e) {
            return super.isCommand(input);
        }
    }

    public boolean validateInput(String[] input) {
        if (input.length != 3 && input.length != 4) {
            return false;
        }

        try {
            ChessPosition start = ChessPosition.parsePosition(input[1]);
            ChessPosition end = ChessPosition.parsePosition(input[2]);
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
            boolean isPromotionRow = end.getRow() == team.getOpposite().getHomeRow();

            boolean isPromotionMove = isPawn && isPromotionRow;

            if (input.length == 4) {
                return isPromotionMove;
            } else {
                return !isPromotionMove;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public State run(String[] input, PrintStream out) {
        try {
            ChessPosition start = ChessPosition.parsePosition(input[1]);
            ChessPosition end = ChessPosition.parsePosition(input[2]);
            ChessGame game = server.game().getGame();
            if (team != game.getTeamTurn()) {
                out.println("Its not your turn yet");
                return null;
            }
            ChessPiece.PieceType promotionPiece = null;
            if (input.length == 4) {
                promotionPiece = ChessPiece.PieceType.parsePromotion(input[3]);
            }
            ChessMove move = new ChessMove(start, end, promotionPiece);
            server.makeMove(move);
            display.updateGame(server.game());
        } catch (Exception e) {
            out.println("Unexpected Error: " + e.getMessage());
        }
        return null;
    }


}