package client;

import client.commands.Command;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CommandLine {
    // This Focuses on taking an input and processing it
    private State state;
    private Integer attachedGameID;
    private final Map<State, Collection<Command>> commands;
    public CommandLine() {
        commands = new HashMap<>();
        state = State.PRELOGIN;
        attachedGameID = null;
    }

    private Collection<Command> getCommands(State state) {
        if (!commands.containsKey(state)) {
            commands.put(state, new LinkedList<>());
        }
        return commands.get(state);
    }

    public void addCommand(Command newCommand, State state) {
        getCommands(state).add(newCommand);
    }

    public State getState() {
        return state;
    }

    public Integer getAttachedGameID() {
        return attachedGameID;
    }

    public void displayHelp() {
        new Command("help", "with chess").displayHelp();
        Collection<Command> commands = getCommands(state);
        for(Command command : commands) {
            command.displayHelp();
        }
        new Command("quit", "chess").displayHelp();
    }

    public void process(String[] input, ServerFacade server) {
        if (input.length < 1) {
            return;
        }
        for (Command command : getCommands(state)) {
            if (!command.isCommand(input[0])) {
                continue;
            }
            if (!command.validateInput(input)) {
                System.out.println("Invalid Input");
                command.displayHelp();
                return;
            }
            try {
                State newState = command.run(input, server);
                if (newState != null) {
                    state = newState;
                }
                attachedGameID = command.getAttachedGameID();
            } catch (Exception e) {
                System.out.println("Unexpected Error: " + e);
            }
            return;
        }
        if ("help".equals(input[0])) {
            displayHelp();
            return;
        } else if ("quit".equals(input[0])) {
            state = State.OFF;
            return;
        }
        System.out.println("Unknown Command: " + input[0]);
    }
}