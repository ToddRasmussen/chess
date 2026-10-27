package client.commands;

import client.internal.State;

import java.io.PrintStream;

public class Command {
    // Generic/Dummy Command (should be inherited from)
    private final String command;
    private final String information;
    private final String prefix;

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
     * @param input from user
     * @return is the array a valid input
     */
    public boolean validateInput(String[] input) {
        return false;
    }

    /**
     *
     * @param input from user
     * @param out stream output
     * @return new state or null if no new state
     */
    public State run(String[] input, PrintStream out) {
        return null;
    }
}