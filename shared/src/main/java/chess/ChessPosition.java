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

    public ChessPosition add(ChessVector vector) {
        return new ChessPosition(
            getRow() + vector.getDeltaRow(),
            getColumn() + vector.getDeltaCol()
        );
    }


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

    @Override
    public int hashCode() {
        return Objects.hash(getRow(), getColumn());
    }
}
