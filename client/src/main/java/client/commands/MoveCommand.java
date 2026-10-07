package client.commands;

import chess.ChessGame;
import client.Display;
import client.ServerFacade;
import client.internal.State;

public class MoveCommand extends Command {
    private final ChessGame.TeamColor team;
    private final Display display;
    /**
     * Command to move a piece on the board
     */
    public MoveCommand(ChessGame.TeamColor team, Display display) {
        super("move", "a piece");
        this.team = team;
        this.display = display;
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;//TODO
    }

    public State run(String[] inputs) {
        //TODO
        try {

            display.triggerRedraw();
            return null;
        } catch (Exception e) {
            System.out.println("Unexpected Error:" + e);
        }
        return null;
    }
}
