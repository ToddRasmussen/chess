package client.Commands;

import client.ServerFacade;
import client.State;

public class Command {
    // Generic/Dummy for command
    private String command;
    private String information;
    private String prefix;

    public Command(String command, String information) {
        this.command = command;
        this.information = information;
        prefix = command.strip().split("\\s+")[0];
    }

    public void displayHelp() {
        String line = "\u001b[36m"+ command + "\u001b[39m" + " - " + information;
        System.out.println(line);
    }

    // is the input this command?
    public boolean isCommand(String input) {
        return prefix.equals(input);
    }

    // is the given input valid?
    public boolean validateInput(String[] input) {
        return false;
    }

    // Returned value is 'null' for it does not care or a value if it changes to that state
    public State run(String[] inputs, ServerFacade server) {
        return null;
    }
}