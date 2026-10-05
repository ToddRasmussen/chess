package client.commands;

import client.ServerFacade;
import client.internal.State;

public class LeaveCommand extends Command {
    private final State state;
    /**
     * Command to leave an active game
     */
    public LeaveCommand(State state) {
        super("leave", "the game");
        this.state = state;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            //TODO
            return State.POSTLOGIN;
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
