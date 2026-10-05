package client.commands;

import client.ServerFacade;
import client.internal.State;

public class ResignCommand extends Command {

    /**
     * Command to resign the game
     */
    public ResignCommand() {
        super("resign", "the game");
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.resign();
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
