package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    //DATA
    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    /**
     *
     * @param startPosition of move
     * @param endPosition of move
     * @param promotionPiece of resulting piece (null if no change)
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    /**
     *
     * @param startPosition of move
     * @param vector from start
     * @param promotionPiece of resulting piece (null if no change)
     */
    public ChessMove(ChessPosition startPosition, ChessVector vector, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = startPosition.add(vector);
        this.promotionPiece = promotionPiece;
    }

    /**
     *
     * @param startPosition of move
     * @param deltaRow from start
     * @param deltaCol from start
     * @param promotionPiece of resulting piece (null if no change)
     */
    public ChessMove(ChessPosition startPosition, int deltaRow, int deltaCol, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = startPosition.add(deltaRow, deltaCol);
        this.promotionPiece = promotionPiece;
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }




    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    /**
     *
     * @param obj   the reference object with which to compare.
     * @return boolean representing if its the same move
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove other) {
            return (
                Objects.equals(this.getStartPosition(), other.getStartPosition()) &&
                Objects.equals(this.getEndPosition(), other.getEndPosition()) &&
                this.getPromotionPiece() == other.getPromotionPiece()
            );
        }
        return false;
    }

    /**
     *
     * @return unique hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }
}
