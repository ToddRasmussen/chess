package client.commands;

import client.ServerFacade;
import client.State;
import client.exceptions.AlreadyTakenException;
import client.exceptions.BadRequestException;

import java.io.IOException;

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
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (BadRequestException e) {
            System.out.println("Invalid gameID: " + inputs[1]);
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }

    public Integer getAttachedGameID() {
        return gameID;
    }
}
