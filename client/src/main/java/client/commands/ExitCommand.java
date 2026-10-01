package client.commands;

import client.ServerFacade;
import client.State;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "the game");
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            return State.POSTLOGIN;
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
