package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;
import client.exceptions.UnauthorizedException;
import model.UserData;

import java.io.IOException;
import java.io.PrintStream;

public class LoginCommand extends Command {
    private final ServerFacade server;
    private final Display display;
    /**
     * command to log in to an account
     */
    public LoginCommand(ServerFacade server, Display display) {
        super("login <USERNAME> <PASSWORD>", "to play chess");
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 3;
    }

    public State run(String[] input, PrintStream out) {
        try {
            server.login(new UserData(input[1], input[2], ""));
            display.clear();
            return State.POSTLOGIN;
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (UnauthorizedException e) {
            out.println("Incorrect Username or Password");
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
