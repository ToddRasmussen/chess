package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    
    //DATA
    private final int row;
    private final int col;

    /**
     *
     * @param row #
     * @param col #
     */
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    /**
     *
     * @param vector to add
     * @return new position
     */
    public ChessPosition add(ChessVector vector) {
        return new ChessPosition(
            getRow() + vector.getDeltaRow(),
            getColumn() + vector.getDeltaCol()
        );
    }

    /**
     *
     * @param deltaRow change in row
     * @param deltaCol change in row
     * @return new position
     */
    public ChessPosition add(int deltaRow, int deltaCol) {
        return new ChessPosition(
            getRow() + deltaRow,
            getColumn() + deltaCol
        );
    }

    /**
     *
     * @return bool representing if position is within the bounds of the board
     */
    public boolean isInBounds() {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    /**
     *
     * @param vector vector to add before checking
     * @return bool representing if resulting position is within the bounds of the board
     */
    public boolean isInBounds(ChessVector vector) {
        return this.add(vector).isInBounds();
    }

    /**
     *
     * @param deltaRow change in row to add before checking
     * @param deltaCol change in col to add before checking
     * @return bool representing if resulting position is within the bounds of the board
     */
    public boolean isInBounds(int deltaRow, int deltaCol) {
        return this.add(deltaRow, deltaCol).isInBounds();
    }

    /**
     *
     * @param obj   the reference object with which to compare.
     * @return bool representing if it equals this position
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPosition other) {
            return (
                this.getRow() == other.getRow() &&
                this.getColumn() == other.getColumn()
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
        return Objects.hash(getRow(), getColumn());
    }

    /**
     *
     * @return string representation
     */
    @Override
    public String toString() {
        char colChar = (char) ('a' + col - 1);
        return "" + colChar + row;
    }

}
