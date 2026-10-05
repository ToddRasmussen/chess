package client.commands;

import client.ServerFacade;
import client.internal.State;
import client.exceptions.BadRequestException;

import java.io.IOException;

public class ObserveCommand extends Command {
    private final ServerFacade server;
    private Integer gameID = null;

    /**
     * command to observe a game
     */
    public ObserveCommand(ServerFacade server) {
        super("observe <ID>", "a game");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 2;
    }

    public State run(String[] inputs) {
        try {
            server.game(Integer.parseInt(inputs[1])); //run to ensure we are looking at a valid game
            gameID = Integer.parseInt(inputs[1]);
            return State.OBSERVER;
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (BadRequestException | NumberFormatException e) {
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
