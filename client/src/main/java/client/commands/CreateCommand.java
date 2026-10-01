package client.commands;

import client.ServerFacade;
import client.State;

public class CreateCommand extends Command {

    public CreateCommand() {
        super("create <NAME>", "a game");
    }

    public boolean validateInput(String[] input) {
        return input.length == 2;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            int gameID = server.newGame(inputs[1]);
            System.out.println(gameID);
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
