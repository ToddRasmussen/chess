package client.commands;

import client.ServerFacade;
import client.State;
import model.UserData;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        super("register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
    }

    public boolean validateInput(String[] input) {
        return input.length == 4;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.register(new UserData(inputs[1], inputs[2], inputs[3]));
            return State.POSTLOGIN;
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println("Exception:" + e);
        }
        return null;
    }
}
