package client.Displays;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.Display;
import model.GameData;

public class TerminalDisplay implements Display {

    public TerminalDisplay() {

    }

    private void printDrawnBoard(String[][] drawnGame) {
        for (String[] row : drawnGame) {
            StringBuilder merged = new StringBuilder();
            for (String cell : row) {
                merged.append(cell);
            }
            System.out.println(merged);
        }
    }

    public void spectateGame(GameData gameData) {
        String title = "Spectating White: " + gameData.getWhiteUsername() + " Vs Black: " + gameData.getBlackUsername();
        System.out.println(title);
        printDrawnBoard(drawBoard(gameData.getGame()));
    }

    public void displayGame(GameData gameData, ChessGame.TeamColor team) {
        boolean isWhite = team == ChessGame.TeamColor.WHITE;
        String opponent =  isWhite ?  gameData.getBlackUsername() : gameData.getWhiteUsername();
        String title = "Playing Against " + opponent + " as " + team.toString();
        System.out.println(title);
        String[][] drawnBoard = drawBoard(gameData.getGame());
        if (team == ChessGame.TeamColor.BLACK) {
            flipDrawnBoard(drawnBoard);
        }
        printDrawnBoard(drawnBoard);
    }


    private Integer getBackgroundCode(ChessPosition position) {
        int sum = position.getColumn() + position.getRow();
        boolean even = sum%2 == 0;
        return even ? 40 : 107;
    }

    private Integer getForegroundCode(ChessGame.TeamColor team) {
        return team == ChessGame.TeamColor.WHITE ? 34 : 31;
    }

    private String[] columnLabels() {
        String[] out = new String[10];
        for (int i = 0; i<10; i++) {
            out[i] = "\u001b[30;47m";
        }
        out[0] += " ";
        for (int i = 1; i<9; i++) {
            out[i] += (char) ('a' + i - 1);
        }
        out[9] += " ";

        return out;
    }

    private String[][] drawBoard(ChessGame game) {
        String[][] out = new String[10][10];
        ChessBoard board = game.getBoard();
        //[row][col]
        out[0] = columnLabels();
        out[9] = columnLabels();
        for (int i = 1; i < 9; i++) {
            out[i][0] = "\u001b[30;47m" + (9-i);
            out[i][9] = "\u001b[30;47m" + (9-i);
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                //out[i][j]
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                out[i][j] = "\u001b[" + getBackgroundCode(position);
                if (piece == null) {
                    out[i][j] += "m ";
                } else {
                    out[i][j] += ";" + getForegroundCode(piece.getTeamColor()) + "m";
                    out[i][j] += piece.getPieceType().getAbrivation();
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
