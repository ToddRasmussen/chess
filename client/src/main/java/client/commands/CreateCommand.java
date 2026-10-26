package client.commands;

import client.ServerFacade;
import client.internal.State;

import java.io.IOException;
import java.io.PrintStream;

public class CreateCommand extends Command {
    private final ServerFacade server;
    /**
     * command to create a new game
     */
    public CreateCommand(ServerFacade server) {
        super("create <NAME>", "a game");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 2;
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            int gameID = server.newGame(inputs[1]);
            out.println(gameID);
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
