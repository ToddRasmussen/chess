package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;
import client.exceptions.BadRequestException;

import java.io.IOException;
import java.io.PrintStream;

public class ObserveCommand extends Command {
    private final ServerFacade server;
    private final Display display;

    /**
     * command to observe a game
     */
    public ObserveCommand(ServerFacade server, Display display) {
        super("observe <ID>", "a game");
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 2;
    }

    public State run(String[] input, PrintStream out) {
        try {
            int gameID = Integer.parseInt(input[1]);
            display.updateGame(server.game(gameID));
            return State.OBSERVER;
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (BadRequestException | NumberFormatException e) {
            out.println("Invalid gameID: " + input[1]);
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
