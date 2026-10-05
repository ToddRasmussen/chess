package client.commands;

import chess.ChessGame;
import client.ServerFacade;
import client.internal.State;

public class MoveCommand extends Command {
    ChessGame.TeamColor team;
    /**
     * Command to move a piece on the board
     */
    public MoveCommand(ChessGame.TeamColor team) {
        super("move", "a piece");
        this.team = team;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;//TODO
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
