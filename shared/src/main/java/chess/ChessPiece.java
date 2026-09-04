package chess;

import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    
    //DATA
    private chessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented"); //TODO
    }

    /**
     * Calculates all the positions a chess piece can move to in a diagonal direction
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: Implement
    }

    /**
     * Calculates all the positions a chess piece can move to in a horizontal direction
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> horizontalMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: Implement
    }

    /**
     * Calculates all the positions a chess piece can move using the given vector
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> vectorMoves(ChessBoard board, ChessPosition myPosition, ChessVector vector) {
        //TODO: Implement
    }

    /**
     * Calculates all the positions a chess piece can move using the 'l' shape knights move in
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: Implement
    }

    /**
     * Calculates all the positions a chess piece can move if its a pawn
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: Implement
    }

    /**
     * Calculates all the positions a chess piece can move if its a king
     * Does not take into account moves that are illegal due to making the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        //TODO: Implement
    }
}
