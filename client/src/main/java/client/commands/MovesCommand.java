package client.commands;

import chess.ChessGame;
import client.Display;
import client.ServerFacade;
import client.internal.State;

public class MovesCommand extends Command {
    private final ChessGame.TeamColor team;
    private final Display display;
    /**
     * Command to display valid moves of a piece
     */
    public MovesCommand(ChessGame.TeamColor team, Display display) {
        super("moves", " the piece can make");
        this.team = team;
        this.display = display;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 2;
    }

    public State run(String[] inputs, ServerFacade server) {
        //TODO
        try {
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
