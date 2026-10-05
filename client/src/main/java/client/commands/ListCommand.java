package client.commands;

import client.ServerFacade;
import client.internal.State;
import model.GameData;

import java.io.IOException;
import java.util.Collection;

public class ListCommand extends Command {

    /**
     * command to list all active games
     */
    public ListCommand() {
        super("list", "games");
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
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
