package client.commands;

import chess.ChessGame;
import client.ServerFacade;
import client.internal.State;

public class MovesCommand extends Command {
    ChessGame.TeamColor team;
    /**
     * Command to display valid moves of a piece
     */
    public MovesCommand(ChessGame.TeamColor team) {
        super("moves", " the piece can make");
        this.team = team;
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
