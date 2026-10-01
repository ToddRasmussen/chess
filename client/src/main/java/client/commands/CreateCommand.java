package client.commands;

import client.ServerFacade;
import client.State;

import java.io.IOException;

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
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
