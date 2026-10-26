package client.commands;

import client.ServerFacade;
import client.internal.State;

import java.io.PrintStream;

public class ResignCommand extends Command {
    private final ServerFacade server;
    /**
     * Command to resign the game
     */
    public ResignCommand(ServerFacade server) {
        super("resign", "the game");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            server.resign();
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
