package client.commands;

import client.ServerFacade;
import client.internal.State;
import client.exceptions.AlreadyTakenException;
import model.UserData;

import java.io.IOException;
import java.io.PrintStream;

public class RegisterCommand extends Command {
    private final ServerFacade server;
    /**
     * command to register a new account
     */
    public RegisterCommand(ServerFacade server) {
        super("register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 4;
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            server.register(new UserData(inputs[1], inputs[2], inputs[3]));
            return State.POSTLOGIN;
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (AlreadyTakenException e) {
            out.println("Username already Taken");
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
