package client.commands;

import client.ServerFacade;
import client.State;
import client.exceptions.AlreadyTakenException;

import java.io.IOException;

public class LogoutCommand extends Command {

    public LogoutCommand() {
        super("logout", "to create an account");
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.logout();
            return State.PRELOGIN;
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
