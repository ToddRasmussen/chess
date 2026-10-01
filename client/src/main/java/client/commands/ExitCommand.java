package client.commands;

import client.ServerFacade;
import client.State;

import java.io.IOException;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "the game");
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            return State.POSTLOGIN;
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
