package client.Interfaces;

import client.Command;
import client.State;
import client.exceptions.InputException;

import java.util.Collection;
import java.util.LinkedList;

public class CommandLine {
    private State state;
    private Collection<Command> preLogin;
    private Collection<Command> postLogin;
    private Collection<Command> blackTeam;
    private Collection<Command> whiteTeam;
    private Collection<Command> observer;
    public CommandLine() {
        preLogin = new LinkedList<>();
        postLogin = new LinkedList<>();
        blackTeam = new LinkedList<>();
        whiteTeam = new LinkedList<>();
        observer = new LinkedList<>();
        state = State.PRELOGIN;
    }

    private Collection<Command> getCommands() {
        return switch (state) {
            case State.POSTLOGIN -> postLogin;
            case State.BLACKTEAM -> blackTeam;
            case State.WHITETEAM -> whiteTeam;
            case State.OBSERVER -> observer;
            default -> preLogin;
        };
    }

    public State getState() {
        return state;
    }

    public void displayHelp() {
        Collection<Command> commands = getCommands();
        for(Command command : commands) {
            command.displayHelp();
        }
    }

    public void process(String[] input) {
        if (input.length < 1) {
            return;
        }
        for (Command command : getCommands()) {
            if (!command.isCommand(input[0])) {
                continue;
            }
            try {
                command.validateInput(input);
            } catch (InputException e) {
                System.out.println("Invalid Input");
                command.displayHelp();
                return;
            }
            try {
                State newState = command.run(input);
                if (newState != null) {
                    state = newState;
                }
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e);
            }
            return;
        }
        if ("quit".equals(input[0])) {
            displayHelp();
            return;
        }
        System.out.println("Unknown Command: " + input[0]);
    }
}