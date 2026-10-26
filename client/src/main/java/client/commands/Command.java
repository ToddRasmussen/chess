package client.commands;

import client.internal.State;

import java.io.PrintStream;

public class Command {
    // Generic/Dummy Command (should be inherited from)
    private String command;
    private String information;
    private String prefix;

    public Command(String command, String information) {
        this.command = command;
        this.information = information;
        prefix = command.strip().split("\\s+")[0];
    }

    /**
     * Displays help message of command
     */
    public void displayHelp(PrintStream out) {
        String line = "\u001b[36m"+ command + "\u001b[39m" + " - " + information;
        out.println(line);
    }

    /**
     *
     * @param input given by user
     * @return if the input is this command
     */
    public boolean isCommand(String input) {
        return prefix.equals(input);
    }

    /**
     *
     * @param inputs from user
     * @return is the array a valid input
     */
    public boolean validateInput(String[] inputs) {
        return false;
    }

    /**
     *
     * @param inputs from user
     * @param out stream output
     * @return new state or null if no new state
     */
    public State run(String[] inputs, PrintStream out) {
        return null;
    }

    /**
     *
     * @return attached gameID
     */
    public Integer getAttachedGameID() {
        return null;
    }
}