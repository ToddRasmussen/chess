package client.commands;

import client.ServerFacade;
import client.internal.State;

public class ExitCommand extends Command {

    /**
     * Command to exit a active game
     */
    public ExitCommand() {
        super("exit", "the game");
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            return State.POSTLOGIN;
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
