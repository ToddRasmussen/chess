package client.Commands;

import client.ServerFacade;
import client.State;
import model.UserData;

public class LoginCommand extends Command {

    public LoginCommand() {
        super("login <USERNAME> <PASSWORD>", "to create an account");
    }

    public boolean validateInput(String[] input) {
        return input.length == 3;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.login(new UserData(inputs[1], inputs[2], ""));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
