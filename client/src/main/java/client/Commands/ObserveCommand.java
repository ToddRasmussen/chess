package client.Commands;

import client.ServerFacade;
import client.State;

public class ObserveCommand extends Command {

    private Integer gameID = null;

    public ObserveCommand() {
        super("observe <ID>", "a game");
    }

    public boolean validateInput(String[] input) {
        return input.length == 2;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.game(Integer.getInteger(inputs[1])); //run to ensure we are looking at a valid game
            gameID = Integer.getInteger(inputs[1]);
            return State.OBSERVER;
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }

    public Integer getAttachedGameID() {
        return gameID;
    }
}
