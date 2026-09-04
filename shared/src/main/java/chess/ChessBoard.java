package chess;

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
        resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board.add(position, piece);
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
     * @return bool on if the position is filled with a piece
     */
    public Bool isFilled(ChessPosition position) {
        return getPiece != null;
    }
    /**
     * Gets the location of the team's king
     * 
     * @param teamColor The team of the king
     * @return position of the king
     */
    public ChessPosition getKing(ChessGame.TeamColor teamColor) {
        //TODO: implement
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        throw new RuntimeException("Not implemented"); //TODO
    }
}
