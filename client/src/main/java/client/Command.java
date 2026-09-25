package client;

import client.exceptions.InputException;

public class Command {
    String command;
    String information;
    String prefix;
    public Command(String command, String information) {
        this.command = command;
        this.information = information;
        this.prefix = command.strip().split("\\s+")[0];
    }

    public void displayHelp() {
        String line = "\u001b[46]"+ command + "\u001b[49]" + " - " + information;
        System.out.println(line);
    }

    public boolean isCommand(String input) {
        return prefix.equals(input);
    }


    public void validateInput(String[] input) throws InputException {
        throw new InputException();
    }

    public State run(String[] inputs) {
        return null;
    }
}