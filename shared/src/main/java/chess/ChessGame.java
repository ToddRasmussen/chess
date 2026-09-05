package chess;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    //DATA
    private ChessBoard board;
    private TeamColor currentTeam;
    private final Queue<ChessMove> moveHistory = new LinkedList<>();
    //TODO: Move History

    public ChessGame() {
        board = new ChessBoard();
        currentTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Swaps which teams turn it is
     */
    public void swapTeamTurn() {
        currentTeam = currentTeam.getOpposite();
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE(1, 1, 2),
        BLACK(-1, 8, 7);

        private final int direction;
        private final int homeRow;
        private final int pawnRow;

        TeamColor(int direction, int homeRow, int pawnRow) {
            this.direction = direction;
            this.homeRow = homeRow;
            this.pawnRow = pawnRow;
        }

        public int getDirection() {
            return this.direction;
        }

        public int getHomeRow() {
            return this.homeRow;
        }

        public int getPawnRow() {
            return this.pawnRow;
        }

        public TeamColor getOpposite() {
            return (this == WHITE) ? BLACK : WHITE;
        }
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;
        return piece.pieceMoves(board, startPosition);
        //TODO: Validate Moves (cant put in check)
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        if (color != currentTeam) throw new InvalidMoveException("Incorrect Team");
        Collection<ChessMove> validMoves = this.validMoves(move.getStartPosition());
        if (!validMoves.contains(move)) throw new InvalidMoveException("Invalid Move");
        //TODO: Move Piece on board
        moveHistory.add(move);
        swapTeamTurn();
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
