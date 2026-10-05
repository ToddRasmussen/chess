package client.commands;

import client.ServerFacade;
import client.internal.State;
import client.exceptions.UnauthorizedException;
import model.UserData;

import java.io.IOException;

public class LoginCommand extends Command {

    /**
     * command to log in to an account
     */
    public LoginCommand() {
        super("login <USERNAME> <PASSWORD>", "to play chess");
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 3;
    }

    public State run(String[] inputs, ServerFacade server) {
        try {
            server.login(new UserData(inputs[1], inputs[2], ""));
            return State.POSTLOGIN;
        } catch (InterruptedException | IOException e) {
            System.out.println("Server Connection Error");
        } catch (UnauthorizedException e) {
            System.out.println("Incorrect Username or Password");
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
