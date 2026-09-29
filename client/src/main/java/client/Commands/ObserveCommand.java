package client.Commands;

import client.ServerFacade;
import client.State;

public class ObserveCommand extends Command {

    public ObserveCommand() {
        super("observe <ID>", "a game");
    }

    public boolean validateInput(String[] input) {
        return input.length == 2;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            //TODO
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
