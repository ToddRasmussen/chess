package client.commands;

import client.ServerFacade;
import client.internal.State;
import client.exceptions.UnauthorizedException;
import model.UserData;

import java.io.IOException;

public class LoginCommand extends Command {
    private final ServerFacade server;
    /**
     * command to log in to an account
     */
    public LoginCommand(ServerFacade server) {
        super("login <USERNAME> <PASSWORD>", "to play chess");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 3;
    }

    public State run(String[] inputs) {
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
