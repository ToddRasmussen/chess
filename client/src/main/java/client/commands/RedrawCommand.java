package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;

import java.io.PrintStream;

public class RedrawCommand extends Command {

    private final Display display;
    private final ServerFacade server;
    /**
     * Command to redraw the active game board (basically just a dummy since its automatic)
     */
    public RedrawCommand(ServerFacade server, Display display) {
        super("redraw", "the board");
        this.display = display;
        this.server = server;
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] input, PrintStream out) {
        try {
            display.updateGame(server.game());
        } catch (Exception e) {
                out.println("Unexpected Error:" + e);
            }
        return null;
    }

}
