package client.commands;

import client.Display;
import client.ServerFacade;
import client.internal.State;

public class RedrawCommand extends Command {

    private final Display display;
    /**
     * Command to redraw the active game board (basically just a dummy since its automatic)
     */
    public RedrawCommand(Display display) {
        super("redraw", "the board");
        this.display = display;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs) {
        display.triggerRedraw();
        return null;
    }

}
