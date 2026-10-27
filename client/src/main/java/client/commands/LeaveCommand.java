package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;

import java.io.PrintStream;

public class LeaveCommand extends Command {
    private final ServerFacade server;
    private final Display display;
    /**
     * Command to leave an active game
     */
    public LeaveCommand(ServerFacade server, Display display) {
        super("leave", "the game");
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] input, PrintStream out) {
        try {
            server.leave();
            display.clear();
            out.println("Successfully left the game.");
            return State.POSTLOGIN;
        } catch (Exception e) {
            out.println("Unexpected Error: " + e.getMessage());
        }
        return null;
    }
}