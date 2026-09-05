package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

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
    private final Deque<ChessMove> moveHistory = new LinkedList<>();

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
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
        Collection<ChessMove> legalMoves = new ArrayList<>();
        if (piece == null) return legalMoves;

        Collection<ChessMove> candidateMoves = piece.pieceMoves(board, startPosition);

        for (ChessMove move : candidateMoves) {
            ChessBoard boardCopy = new ChessBoard(this.board);

            boardCopy.removePiece(move.getStartPosition());

            if (move.getPromotionPiece() == null) {
                boardCopy.addPiece(move.getEndPosition(), piece);
            } else {
                boardCopy.addPiece(move.getEndPosition(), new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            }

            if (!isInCheck(piece.getTeamColor(), boardCopy)) {
                legalMoves.add(move);
            }
        }

        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) throw new InvalidMoveException("No piece at starting position");
        TeamColor color = board.getPiece(move.getStartPosition()).getTeamColor();
        if (color != currentTeam) throw new InvalidMoveException("Incorrect Team");
        if (!validMoves(move.getStartPosition()).contains(move)) throw new InvalidMoveException("Invalid Move");

        ChessPiece piece = board.removePiece(move.getStartPosition());

        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), piece); 
        } else {
            board.addPiece(move.getEndPosition(), new ChessPiece(color, move.getPromotionPiece()));
        }
        piece.updateMoved();
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
        return isInCheck(teamColor, this.board);
    }

    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        ChessPosition kingPosition = board.getKingPosition(teamColor);

        for (Map.Entry<ChessPosition, ChessPiece> entry : board.entrySet()) {
            ChessPosition position = entry.getKey();
            ChessPiece piece = entry.getValue();

            if (piece.getTeamColor() != teamColor) {
                for (ChessMove move : piece.pieceMoves(board, position)) {
                    if (move.getEndPosition().equals(kingPosition)) {
                        return true; // Enemy piece can attack the King's square
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheckmate(teamColor, this.board);
    }

    public boolean isInCheckmate(TeamColor teamColor, ChessBoard board) {
        return isInCheck(teamColor) && canMove(teamColor);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && canMove(teamColor);
    }


    private boolean canMove(TeamColor teamColor) {
        for (Map.Entry<ChessPosition, ChessPiece> entry : board.entrySet()) {
            ChessPosition position = entry.getKey();
            ChessPiece piece = entry.getValue();

            if (piece.getTeamColor() == teamColor) {
                if (!validMoves(position).isEmpty()) {
                    return false; // Found a legal move
                }
            }
        }
        return true;
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

    public ChessMove getLastMove() {
        return moveHistory.peekLast();
    }

    public ChessMove getLastMove(ChessGame.TeamColor team) {
        Iterator<ChessMove> iterator = moveHistory.descendingIterator();

        boolean lastMoveWasWhite = (moveHistory.size() % 2 != 0);
        boolean lastMoveIsRequestedTeam = (team == ChessGame.TeamColor.WHITE) == lastMoveWasWhite;

        if (!lastMoveIsRequestedTeam && iterator.hasNext()) {
            iterator.next();
        }

        return iterator.hasNext() ? iterator.next() : null;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessGame other) {
            return Objects.equals(this.board, other.board) &&
                this.currentTeam == other.currentTeam;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTeam);
    }
}
