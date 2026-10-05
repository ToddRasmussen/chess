package client.displays;

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

    public void display(Integer gameID, ServerFacade server, State state) {
        if (lastState == null) {
            System.out.println(EscapeSequences.ERASE_SCREEN);
        }
    }
}
