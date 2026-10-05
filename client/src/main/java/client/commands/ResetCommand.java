package client.commands;

import client.ServerFacade;
import client.internal.State;

import java.io.IOException;

public class ResetCommand extends Command {
    private final ServerFacade server;
    /**
     * command to tell server to reset
     */
    public ResetCommand(ServerFacade server) {
        super("reset", "the server");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs) {
        try {
            server.reset();
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
