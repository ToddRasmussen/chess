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

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;
    }

    public ChessMove(ChessPosition startPosition, ChessVector vector, ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = startPosition.add(vector);
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

    public ChessVector getVector() {
        return new ChessVector(
            getEndPosition().getRow() - getStartPosition().getRow(),
            getEndPosition().getColumn() - getStartPosition().getColumn()
        );
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

    //TODO: Verify this is how you do it
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessMove other) {
            return (
                this.getStartPosition() == other.getStartPosition() &&
                this.getEndPosition() == other.getEndPosition() &&
                // We might want to remove this later as strictly speaking the promotionPiece
                // is irrelevant to if the move in equal to another
                this.getPromotionPiece() == other.getPromotionPiece()
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartPosition(), getEndPosition(), getPromotionPiece());
    }
}
