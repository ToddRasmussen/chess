package client.displays;

import chess.ChessPosition;
import client.internal.State;
import model.GameData;

import java.io.PrintStream;

public interface Display {

    void updateGame(GameData game);
    void clear();
    void highlight(ChessPosition position);

    PrintStream getPrintStream();
    String[] getInput();

    void init();
    void tick(State state);
    void close();
}
