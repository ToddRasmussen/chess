package client.Commands;

import client.ServerFacade;
import client.State;
import model.GameData;
import model.UserData;

import java.util.Collection;

public class ListCommand extends Command {

    public ListCommand() {
        super("list", "games");
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            Collection<GameData> games = server.games();
            for (GameData game : games) {
                System.out.println(game.toString());
            }
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
