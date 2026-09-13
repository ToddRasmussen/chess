package model;

import chess.ChessGame;


public class GameData {

    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;

    public GameData(String gameName, ChessGame game) {
        this.gameName = gameName;
        this.game = game;
    }
    
    public GameData(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }


    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String username) {
        whiteUsername = username;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String username) {
        blackUsername = username;
    }


    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

}