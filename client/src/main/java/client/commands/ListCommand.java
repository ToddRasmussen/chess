package client.commands;

import client.ServerFacade;
import client.internal.State;
import model.GameData;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;

public class ListCommand extends Command {
    private final ServerFacade server;
    /**
     * command to list all active games
     */
    public ListCommand(ServerFacade server) {
        super("list", "games");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            Collection<GameData> games = server.games();
            boolean flag = false;
            for (GameData game : games) {
                out.println(game.toString());
                flag = true;
            }
            if (!flag) {
                out.println("No Games to List");
            }
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
