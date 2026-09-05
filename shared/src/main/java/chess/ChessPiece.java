package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    //DATA
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    private int moveCount;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.moveCount = 0;
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
        PAWN;

        public boolean canPromote() {
            return this == PAWN;
        }

    }

    private Collection<PieceType> getPromotionOptions() {
        Collection<PieceType> collection = new HashSet<>();

        collection.add(PieceType.QUEEN);
        collection.add(PieceType.BISHOP);
        collection.add(PieceType.KNIGHT);
        collection.add(PieceType.ROOK);

        return collection;
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
        Collection<ChessMove> collection = new HashSet<>();
        switch (board.getPiece(myPosition).getPieceType()) {
            case KING -> collection.addAll(kingMoves(board, myPosition));
            case QUEEN -> {
                collection.addAll(diagonalMoves(board, myPosition));
                collection.addAll(horizontalMoves(board, myPosition));
            }
            case BISHOP -> collection.addAll(diagonalMoves(board, myPosition));
            case KNIGHT -> collection.addAll(knightMoves(board, myPosition));
            case ROOK -> collection.addAll(horizontalMoves(board, myPosition));
            case PAWN -> collection.addAll(pawnMoves(board, myPosition));
            default -> throw new IllegalArgumentException("Invalid piece type");
        }
        return collection;
    }

    /**
     * Calculates all the positions a chess piece can move to in a diagonal direction
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> diagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> collection = new HashSet<>();

        for (int deltaRow : new int[]{-1, 1}) {
            for (int deltaCol : new int[]{-1, 1}) {
                collection.addAll(vectorMoves(board, myPosition, new ChessVector(deltaRow, deltaCol)));
            }
        }

        return collection;
    }

    /**
     * Calculates all the positions a chess piece can move to in a horizontal direction
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> horizontalMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> collection = new HashSet<>();

        for (int deltaRow : new int[]{-1, 1}) {
            collection.addAll(vectorMoves(board, myPosition, new ChessVector(deltaRow, 0)));
        }
        for (int deltaCol : new int[]{-1, 1}) {
            collection.addAll(vectorMoves(board, myPosition, new ChessVector(0, deltaCol)));
        }
        return collection;
    }

    /**
     * Calculates all the positions a chess piece can move using the given vector
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> vectorMoves(ChessBoard board, ChessPosition myPosition, ChessVector vector) {
        Collection<ChessMove> collection = new HashSet<>();
        ChessGame.TeamColor color = board.getPieceColor(myPosition);
        int scalar = 1;

        while (true) {
            ChessPosition newPosition = myPosition.add(vector.multiply(scalar));
        
            if (!newPosition.isInBounds()) {
                break;
            }

            if (!board.isFilled(newPosition, color)) {
                collection.add(new ChessMove(myPosition, newPosition, null));
            }

            if (board.isFilled(newPosition)) break;

            scalar++;
        }

        return collection;
    }

    /**
     * Calculates all the positions a chess piece can move using the 'l' shape knights move in
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> collection = new HashSet<>();
        ChessGame.TeamColor color = board.getPieceColor(myPosition);
        ChessVector[] vectors = {
            new ChessVector(-2, -1), new ChessVector(-2, 1), new ChessVector(-1, -2), new ChessVector(-1, 2),
            new ChessVector(1, -2), new ChessVector(1, 2), new ChessVector(2, -1), new ChessVector(2, 1)
        };

        for (ChessVector vector : vectors) {
            if (myPosition.isInBounds(vector) && !board.isFilled(myPosition.add(vector), color)) {
                collection.add(new ChessMove(myPosition, vector, null));
            }
        }
        return collection;
    }

    /**
     * Calculates all the positions a chess piece can move if its a pawn
     * Does not take into account moves that are illegal due to leaving the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> collection = new HashSet<>();
        ChessGame.TeamColor color = board.getPieceColor(myPosition);

        ChessVector vector = new ChessVector(color.getDirection(),0);
        if (myPosition.isInBounds(vector) && !board.isFilled(myPosition.add(vector))) {
            if (myPosition.add(vector).getRow() == color.getOpposite().getHomeRow()) {
                for (PieceType type : getPromotionOptions()) {
                    collection.add(new ChessMove(myPosition, vector, type));
                }
            } else {
                collection.add(new ChessMove(myPosition, vector, null));
            }
            vector = vector.multiply(2);
            if (myPosition.getRow() == color.getPawnRow() && myPosition.isInBounds(vector) && !board.isFilled(myPosition.add(vector))) {
                collection.add(new ChessMove(myPosition, vector, null));
            }
        }

        for (int deltaCol : new int[]{-1, 1}) {
            ChessPosition newPosition = myPosition.add(color.getDirection(),deltaCol);
            if (board.isFilled(newPosition, color.getOpposite())) {
                if (newPosition.getRow() == color.getOpposite().getHomeRow()) {
                    for (PieceType type : getPromotionOptions()) {
                        collection.add(new ChessMove(myPosition, newPosition, type));
                    }
                } else {
                    collection.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }

        // TODO: Special Move Rule (En Passant)

        return collection;
    }

    /**
     * Calculates all the positions a chess piece can move if its a king
     * Does not take into account moves that are illegal due to making the king in
     * danger or being incorrect type of piece
     *
     * @return Collection of valid moves
     */
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> collection = new HashSet<>();
        ChessGame.TeamColor color = board.getPieceColor(myPosition);
        for (int deltaRow : new int[]{-1, 0, 1}) {
            for (int deltaCol : new int[]{-1, 0, 1}) {
                if (myPosition.isInBounds(deltaRow, deltaCol) && !board.isFilled(myPosition.add(deltaRow, deltaCol), color)) {
                    collection.add(new ChessMove(myPosition, deltaRow, deltaCol, null));
                }
            }
        }
        // TODO: Castling
        return collection;
    }

    public void updateMoved() {
        moveCount++;
    }

    public boolean getHasMoved() {
        return moveCount > 0;
    }

    public int getMoveCount() {
        return moveCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ChessPiece other) {
            return (
                this.getPieceType() == other.getPieceType() &&
                this.getTeamColor() == other.getTeamColor()
            );
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPieceType(), getTeamColor());
    }
}
