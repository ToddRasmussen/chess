package client.commands;

import client.ServerFacade;
import client.internal.State;
import client.exceptions.AlreadyTakenException;
import client.exceptions.BadRequestException;

import java.io.IOException;
import java.io.PrintStream;

public class JoinCommand extends Command {
    private final ServerFacade server;
    private Integer gameID = null;

    /**
     * command to join a game
     */
    public JoinCommand(ServerFacade server) {
        super("join <ID> <WHITE|BLACK>", "a game");
        this.server = server;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 3;
    }

    public State run(String[] inputs, PrintStream out) {
        try {
            server.joinGame(Integer.parseInt(inputs[1]), inputs[2].toUpperCase());
            out.println("Joined game: " + inputs[1]);
            gameID = Integer.parseInt(inputs[1]);
            return "white".equalsIgnoreCase(inputs[2]) ? State.WHITETEAM : State.BLACKTEAM;
        } catch (InterruptedException | IOException e) {
            out.println("Server Connection Error");
        } catch (AlreadyTakenException e) {
            out.println("Team " + inputs[2] + " already taken");
        } catch (BadRequestException | NumberFormatException e) {
                out.println("Invalid GameID");
        } catch (Exception e) {
            out.println("Unexpected Error:" + e);
        }
        return null;
    }

    public Integer getAttachedGameID() {
        return gameID;
    }
}
