package model;

import chess.ChessGame;


public class GameData {

    private String whiteUsername;
    private String blackUsername;
    private final int gameID;
    private final String gameName;
    private final ChessGame game;

    public GameData(int gameID, String gameName, ChessGame game) {
        this.gameName = gameName;
        this.game = game;
        this.gameID = gameID;
    }
    
    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
        this.gameID = gameID;
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

    public int getGameID() {
        return gameID;
    }
    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

}