package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;
import client.exceptions.AlreadyTakenException;
import model.UserData;

import java.io.IOException;
import java.io.PrintStream;

public class RegisterCommand extends Command {
    private final ServerFacade server;
    private final Display display;
    /**
     * command to register a new account
     */
    public RegisterCommand(ServerFacade server, Display display) {
        super("register <USERNAME> <PASSWORD> <EMAIL>", "to create an account");
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 4;
    }

    public State run(String[] input, PrintStream out) {
        try {
            server.register(new UserData(input[1], input[2], input[3]));
            display.clear();
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
