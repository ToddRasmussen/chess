package client.commands;

import client.ServerFacade;
import client.State;
import model.GameData;

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
            boolean flag = false;
            for (GameData game : games) {
                System.out.println(game.toString());
                flag = true;
            }
            if (!flag) {
                System.out.println("No Games to List");
            }
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
