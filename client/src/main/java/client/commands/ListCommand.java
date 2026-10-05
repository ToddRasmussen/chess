package client.commands;

import client.ServerFacade;
import client.internal.State;
import model.GameData;

import java.io.IOException;
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

    public State run(String[] inputs) {
        try {
            Collection<GameData> games = server.games();
            boolean flag = false;
            for (GameData game : games) {
                System.out.println(game.toString());
                flag = true;
            }
            if (!flag) {
                System.out.println("No Games to List");
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
