package chess;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    
    //DATA
    private Map<ChessPosition, ChessPiece> board;

    public ChessBoard() {
        board = new HashMap<>();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board.get(position);
    }

    /**
     * Is the given position filled with a piece
     * 
     * @param position The position to check
     * @return boolean on if the position is filled with a piece
     */
    public boolean isFilled(ChessPosition position) {
        return (getPiece(position) != null);
    }

    /**
     * Is the given position filled with a piece of given color
     * 
     * @param position The position to check
     * @return boolean on if the position is filled with a piece
     */
    public boolean isFilled(ChessPosition position, ChessGame.TeamColor color) {
        return (getPiece(position) != null && getPiece(position).getTeamColor() == color);
    }

    /**
     * Gets the location of the team's king
     * 
     * @param teamColor The team of the king
     * @return position of the king
     */
    public ChessPosition getKing(ChessGame.TeamColor teamColor) {
        for (ChessPosition position : board.keySet()) {
            ChessPiece piece = board.get(position);
            if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                return position;
            }
        }
        throw new RuntimeException("King not found");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        // r n b q k b n r
        // p p p p p p p p
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // . . . . . . . .
        // P P P P P P P P
        // R N B Q K B N R

        board = new HashMap<>();
        Queue<ChessPiece.PieceType> base = new LinkedList<>();
        
        base.add(ChessPiece.PieceType.ROOK);
        base.add(ChessPiece.PieceType.KNIGHT);
        base.add(ChessPiece.PieceType.BISHOP);
        base.add(ChessPiece.PieceType.QUEEN);
        base.add(ChessPiece.PieceType.KING);
        base.add(ChessPiece.PieceType.BISHOP);
        base.add(ChessPiece.PieceType.KNIGHT);
        base.add(ChessPiece.PieceType.ROOK);

        // Black Base
        // r n b q k b n r
        ChessGame.TeamColor color = ChessGame.TeamColor.BLACK;
        int row = 8;
        int col = 1;
        for (ChessPiece.PieceType type : base) {
            board.put(new ChessPosition(row, col), new ChessPiece(color, type));
            col++;
        }

        // Black Pawns
        // p p p p p p p p
        row = 7;
        for (col = 1; col <= 8; col++) {
            board.put(new ChessPosition(row, col), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }

        // Pawns
        // P P P P P P P P
        color = ChessGame.TeamColor.WHITE;
        row = 2;
        for (col = 1; col <= 8; col++) {
            board.put(new ChessPosition(row, col), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }

        // Base
        // R N B Q K B N R
        row = 1;
        col = 1;
        for (ChessPiece.PieceType type : base) {
            board.put(new ChessPosition(row, col), new ChessPiece(color, type));
            col++;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessBoard other) {
            return (
                Objects.equals(this.board, other.board)
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
