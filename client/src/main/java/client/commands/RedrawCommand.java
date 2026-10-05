package client.commands;

public class RedrawCommand extends Command {

    /**
     * Command to redraw the active game board (basically just a dummy since its automatic)
     */
    public RedrawCommand() {
        super("redraw", "the board");
    }

    public boolean validateInput(String[] inputs) {
        return inputs.length == 1;
    }

}
