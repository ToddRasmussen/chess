package client.commands;

import client.ServerFacade;
import client.internal.State;

import java.io.PrintStream;

public class LeaveCommand extends Command {
    private final ServerFacade server;
    private final State state;

    /**
     * Command to leave an active game
     */
    public LeaveCommand(ServerFacade server, State state) {
        super("leave", "the game");
        this.server = server;
        this.state = state;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            server.leave();
            out.println("Successfully left the game.");
            return State.POSTLOGIN;
        } catch (Exception e) {
            out.println("Unexpected Error: " + e.getMessage());
        }
        return null;
    }
}