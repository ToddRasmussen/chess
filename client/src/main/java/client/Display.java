package client;

import chess.ChessGame;
import model.GameData;

public interface Display {

    public void spectateGame(GameData gameData);
    public void displayGame(GameData gameData, ChessGame.TeamColor team);
}
