package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;

import java.io.IOException;
import java.io.PrintStream;

public class ResetCommand extends Command {
    private final ServerFacade server;
    private final Display display;
    /**
     * command to tell server to reset
     */
    public ResetCommand(ServerFacade server, Display display) {
        super("reset", "the server");
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 1;
    }

    public State run(String[] input, PrintStream out) {
        try {
            server.reset();
            display.clear();
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
