package client.Commands;

import client.ServerFacade;
import client.State;
import model.UserData;

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
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
