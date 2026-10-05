package client.commands;

import client.ServerFacade;
import client.internal.State;
import client.exceptions.AlreadyTakenException;
import model.UserData;

import java.io.IOException;

public class RegisterCommand extends Command {

    /**
     * command to register a new account
     */
    public RegisterCommand() {
        super("register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 4;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.register(new UserData(inputs[1], inputs[2], inputs[3]));
            return State.POSTLOGIN;
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (AlreadyTakenException e) {
            System.out.println("Username already Taken");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
