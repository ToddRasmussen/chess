package client.Commands;

import client.ServerFacade;
import client.State;
import model.UserData;

public class RegisterCommand extends Command {

    public RegisterCommand() {
        super("login <USERNAME> <PASSWORD>", "to create an account");
    }

    public boolean validateInput(String[] input) {
        return input.length == 4;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.register(new UserData(inputs[1], inputs[2], inputs[3]));
        } catch (Exception e) {
            //TODO use specific exceptions
            System.out.println(e.getMessage());
        }
        return null;
    }
}
