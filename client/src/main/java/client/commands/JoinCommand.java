package client.commands;

import client.ServerFacade;
import client.State;

public class JoinCommand extends Command {

    private Integer gameID = null;

    public JoinCommand() {
        super("join <ID> <WHITE|BLACK>", "a game");
    }

    public boolean validateInput(String[] input) {
        return input.length == 3;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.joinGame(Integer.parseInt(inputs[1]), inputs[2].toUpperCase());
            System.out.println("Joined game: " + inputs[1]);
            gameID = Integer.parseInt(inputs[1]);
            return "white".equalsIgnoreCase(inputs[2]) ? State.WHITETEAM : State.BLACKTEAM;
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
