package client.displays;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import client.internal.ServerFacade;
import client.internal.State;
import model.GameData;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class TerminalDisplay implements Display {
    private final Scanner scanner = new Scanner(System.in);
    private final ServerFacade server;
    private GameData game;
    private boolean gameUpdated = false;
    private List<ChessPosition> highlighted = new LinkedList<>();

    public TerminalDisplay(ServerFacade server) {
        this.server = server;
    }

    public void updateGame(GameData game) {
        this.game = game;
        highlighted.clear();
        gameUpdated = true;
    }

    public void highlight(ChessPosition position) {
        highlighted.add(position);
        gameUpdated = true;
    }

    private PrintStream out = System.out;
    public void setOutput(PrintStream out) {
        this.out = out;
    }

    private int clearSize = 100;
    public void setClearSize(int size) {
        clearSize = size;
    }
    public void clear() {
        for (int i = 0; i < clearSize; i++) {
            out.println();
        }
    }

    public PrintStream getPrintStream() {
        return out;
    }

    public String[] getInput() {
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? new String[0] : line.split("\\s+");
    }

    public void init() {
        clear();
    }

    public void tick(State state) {
        switch (state) {
            case State.OFF -> {return;}
            case State.WHITETEAM -> whiteTeam();
            case State.BLACKTEAM -> blackTeam();
            case State.OBSERVER -> spectator();
        }
        out.print(state.prefix());
    }

    public void close() {
        //Nothing to do
    }


    private void blackTeam() {
        if (gameUpdated) {
            clear();
            out.println("Playing Against " + game.getWhiteUsername());
            printDrawnBoard(flipDrawnBoard(drawBoard(game.getGame())));
            gameUpdated = false;
        }
    }

    private void whiteTeam() {
        if (gameUpdated) {
            clear();
            out.println("Playing Against " + game.getBlackUsername());
            printDrawnBoard(drawBoard(game.getGame()));
            gameUpdated = false;
        }
    }

    private void spectator() {
        if (gameUpdated) {
            clear();
            String title = "Spectating White: " + game.getWhiteUsername() + " Vs Black: " + game.getBlackUsername();
            out.println(title);
            printDrawnBoard(drawBoard(game.getGame()));
            gameUpdated = false;
        }
    }


    private void printDrawnBoard(String[][] drawnGame) {
        for (String[] row : drawnGame) {
            StringBuilder merged = new StringBuilder();
            for (String cell : row) {
                merged.append(cell);
            }
            out.println(merged + "\u001b[39;49m");
        }
    }

    private String[][] flipDrawnBoard(String[][] drawnBoard) {
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
        return drawnBoard;
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
                ChessPosition position = new ChessPosition(9 - i, j);
                ChessPiece piece = board.getPiece(position);
                out[i][j] = "\u001b[" + getBackgroundCode(position);

                if (piece == null) {
                    out[i][j] += "m" + extend + " " + extend;
                } else {
                    out[i][j] += ";" + getForegroundCode(piece.getTeamColor()) + "m";
                    out[i][j] += extend;
                    out[i][j] += piece.getPieceType().getAbrivation();
                    out[i][j] += extend;
                }
            }
        }
        return out;
    }


    private String extend = " ".repeat(5);;
    public void setWidth(int width) {
        //TODO
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


    private Integer getForegroundCode(ChessGame.TeamColor team) {
        return team == ChessGame.TeamColor.WHITE ? 34 : 31;
    }

    private Integer getBackgroundCode(ChessPosition position) {
        if (highlighted.contains(position)) {
            return 102;
        }
        int sum = position.getColumn() + position.getRow();
        boolean even = sum%2 == 0;
        return even ? 107 : 40;
    }
}
