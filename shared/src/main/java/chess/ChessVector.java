package chess;

import java.util.Objects;

/**
 * Vector Class for Chess Game
 */
public class ChessVector {

    //DATA
    private final int deltaRow;
    private final int deltaCol;

    /**
     *
     * @param deltaRow change in row
     * @param deltaCol change in col
     */
    public ChessVector(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     *
     * @return change in row
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     *
     * @return change in col
     */
    public int getDeltaCol() {
        return deltaCol;
    }

    /**
     *
     * @param scalar scalar value to multiple vector by
     * @return new vector scaled using scalar
     */
    public ChessVector multiply(int scalar) {
        return new ChessVector(
            getDeltaRow() * scalar,
            getDeltaCol() * scalar
        );
    }

    /**
     *
     * @param obj   the reference object with which to compare.
     * @return if it equals this vector
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessVector other) {
            return (
                this.getDeltaRow() == other.getDeltaRow() &&
                this.getDeltaCol() == other.getDeltaCol()
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
        return Objects.hash(getDeltaRow(), getDeltaCol());
    }
}