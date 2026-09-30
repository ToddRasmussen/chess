package client.Displays;

import chess.ChessGame;
import client.Display;
import model.GameData;

public class TerminalDisplay implements Display {

    public TerminalDisplay() {

    }

    public void spectateGame(GameData gameData) {
        String title = "Spectating White: " + gameData.getWhiteUsername() + " Vs Black: " + gameData.getBlackUsername();
        System.out.println(title);
    }

    public void displayGame(GameData gameData, ChessGame.TeamColor team) {
        boolean isWhite = team == ChessGame.TeamColor.WHITE;
        String opponent =  isWhite ?  gameData.getBlackUsername() : gameData.getWhiteUsername();
        String title = "Playing Against " + opponent + " as " + team.toString();
        System.out.println(title);
    }


    private String[] drawBoard(ChessGame game) {
        String[] out = new String[10];





        return out;
    }

}
