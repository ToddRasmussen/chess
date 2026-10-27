package client.commands;

import client.internal.ServerFacade;
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

    public boolean validateInput(String[] input) {
        return input.length == 2;
    }

    public State run(String[] input, PrintStream out) {
        try {
            int gameID = server.newGame(input[1]);
            out.println(gameID);
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
