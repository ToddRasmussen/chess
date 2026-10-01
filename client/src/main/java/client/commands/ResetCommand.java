package client.commands;

import client.ServerFacade;
import client.State;

public class ResetCommand extends Command {

    public ResetCommand() {
        super("reset", "the server");
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.reset();
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
