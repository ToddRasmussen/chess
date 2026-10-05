package client.displays;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.Display;
import client.ServerFacade;
import client.internal.State;
import model.GameData;
import ui.EscapeSequences;

public class StaticDisplay implements Display {

    // extend string to add to beginning and end of each cell in order to extend its size
    private final String extend;

    private GameData lastState;

    public StaticDisplay(Integer width) {
        extend  = " ".repeat((width-1)/2);
        lastState = null;
    }

    public void triggerRedraw() {
        lastState = null;
    }

    public void display(Integer gameID, ServerFacade server, State state) {
        if (lastState == null) {
            System.out.println(EscapeSequences.ERASE_SCREEN);

        }



    }

    private Integer getBackgroundCode(ChessPosition position) {
        int sum = position.getColumn() + position.getRow();
        boolean even = sum%2 == 0;
        return even ? 107 : 40;
    }

    private String[] columnLabels() {
        String[] out = new String[10];
        for (int i = 0; i<10; i++) {
            out[i] = "\u001b[30;47m";
        }
        out[0] += extend + " " + extend;
        for (int i = 1; i<9; i++) {
            out[i] += extend + (char) ('a' + i - 1) + extend;
        }
        out[9] += extend + " " + extend;
        return out;
    }

    private String[][] drawBoard(ChessGame game) {
        String[][] out = new String[10][10];
        ChessBoard board = game.getBoard();
        //[row][col]
        out[0] = columnLabels();
        out[9] = columnLabels();
        for (int i = 1; i < 9; i++) {
            out[i][0] = "\u001b[30;47m" + extend + (9-i) + extend;
            out[i][9] = "\u001b[30;47m" + extend + (9-i) + extend;
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                //out[i][j]
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                out[i][j] = "\u001b[" + getBackgroundCode(position);
                if (piece == null) {
                    out[i][j] += "m " + extend + extend;
                } else {
                    out[i][j] += "m";
                    out[i][j] += extend;
                    out[i][j] += piece.getPieceType().getAbrivation();
                    out[i][j] += extend;
                }
            }
        }
        return out;
    }

    public void flipDrawnBoard(String[][] drawnBoard) {
        //flip over row
        for (int r = 0; r < 5; r++) {
            String[] tempRow = drawnBoard[r];
            drawnBoard[r] = drawnBoard[9 - r];
            drawnBoard[9 - r] = tempRow;
        }
        //flip over col
        for (int r = 0; r < 10; r++) {
            for (int c = 0; c < 5; c++) {
                String temp = drawnBoard[r][c];
                drawnBoard[r][c] = drawnBoard[r][9 - c];
                drawnBoard[r][9 - c] = temp;
            }
        }
    }
}
