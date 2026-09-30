package client;

import chess.ChessGame;
import model.GameData;

public interface Display {

    public void display(Integer gameID, ServerFacade server, State state);
}
