package chess;

public class ChessVector {
    
    //DATA
    private int deltaRow;
    private int deltaCol;

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
        )
    }
}