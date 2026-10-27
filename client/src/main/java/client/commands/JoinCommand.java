package client.commands;

import client.displays.Display;
import client.internal.ServerFacade;
import client.internal.State;
import client.exceptions.AlreadyTakenException;
import client.exceptions.BadRequestException;

import java.io.IOException;
import java.io.PrintStream;

public class JoinCommand extends Command {
    private final ServerFacade server;
    private final Display display;

    /**
     * command to join a game
     */
    public JoinCommand(ServerFacade server, Display display) {
        super("join <ID> <WHITE|BLACK>", "a game");
        this.server = server;
        this.display = display;
    }

    public boolean validateInput(String[] input) {
        return input.length == 3;
    }

    public State run(String[] input, PrintStream out) {
        try {
            int gameID = Integer.parseInt(input[1]);
            server.joinGame(gameID, input[2].toUpperCase());
            display.updateGame(server.game(gameID));
            out.println("Joined game: " + input[1]);
            return "white".equalsIgnoreCase(input[2]) ? State.WHITETEAM : State.BLACKTEAM;
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (AlreadyTakenException e) {
            out.println("Team " + input[2] + " already taken");
        } catch (BadRequestException | NumberFormatException e) {
                out.println("Invalid GameID");
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
