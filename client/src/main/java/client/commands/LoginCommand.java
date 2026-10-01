package client.commands;

import client.ServerFacade;
import client.State;
import model.UserData;

public class LoginCommand extends Command {

    public LoginCommand() {
        super("login <USERNAME> <PASSWORD>", "to play chess");
    }

    public boolean validateInput(String[] input) {
        return input.length == 3;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.login(new UserData(inputs[1], inputs[2], ""));
            return State.POSTLOGIN;
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
