package chess;

import java.util.Objects;

public class ChessVector {
    
    //DATA
    private final int deltaRow;
    private final int deltaCol;

    public ChessVector(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    public int getDeltaRow() {
        return deltaRow;
    }
    public int getDeltaCol() {
        return deltaCol;
    }

    public ChessVector multiply(int scalar) {
        return new ChessVector(
            getDeltaRow() * scalar,
            getDeltaCol() * scalar
        );
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(getDeltaRow(), getDeltaCol());
    }
}