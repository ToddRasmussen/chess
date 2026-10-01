package client.commands;

import client.ServerFacade;
import client.State;
import client.exceptions.AlreadyTakenException;

import java.io.IOException;

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
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (AlreadyTakenException e) {
            System.out.println("Team " + inputs[2] + " already taken");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }

    public Integer getAttachedGameID() {
        return gameID;
    }
}
