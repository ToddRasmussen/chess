package client;

import client.commands.Command;
import client.internal.State;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class CommandEngine {
    private State state;
    private final Map<State, Collection<Command>> commands;
    private final Deque<CommandHistory> history;

    public record CommandHistory(State state, String[] inputs, String output) {}
    public record CommandResult(State state, String[] inputs, String output) {}

    public CommandEngine() {
        commands = new HashMap<>();
        state = State.PRELOGIN;
        history = new ArrayDeque<>();
    }

    public void addCommand(Command newCommand, State state) {
        getCommands(state).add(newCommand);
    }

    private Collection<Command> getCommands(State state) {
        return commands.computeIfAbsent(state, k -> new LinkedList<>());
    }

    public State getState() {
        return state;
    }

    public void displayHelp(PrintStream out) {
        new Command("help", "with chess").displayHelp(out);
        Collection<Command> commandList = getCommands(state);
        for (Command command : commandList) {
            command.displayHelp(out);
        }
        new Command("quit", "chess").displayHelp(out);
    }

    public CommandResult process(String[] input) {
        return process(input, null);
    }

    public CommandResult process(String[] input, PrintStream out) {
        if (input.length < 1) {
            return new CommandResult(state, input, "");
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream interceptedOut = new PrintStream(new OutputStream() {
            public void write(int b) {
                buffer.write(b);
                if (out != null) {
                    out.write(b);
                }
            }

            public void write(byte[] b, int off, int len) {
                buffer.write(b, off, len);
                if (out != null) {
                    out.write(b, off, len);
                }
            }

            public void flush() throws IOException {
                buffer.flush();
                if (out != null) {
                    out.flush();
                }
            }
        }, true);

        for (Command command : getCommands(state)) {
            if (!command.isCommand(input[0].toLowerCase())) {
                continue;
            }
            if (!command.validateInput(input)) {
                interceptedOut.println("Invalid Input");
                command.displayHelp(interceptedOut);
                return finalizeExecution(input, buffer);
            }
            try {
                State newState = command.run(input, interceptedOut);
                if (newState != null) {
                    state = newState;
                }
            } catch (Exception e) {
                interceptedOut.println("Unexpected Error: " + e);
            }
            return finalizeExecution(input, buffer);
        }

        if ("help".equals(input[0])) {
            displayHelp(interceptedOut);
        } else if ("quit".equals(input[0])) {
            state = State.OFF;
            interceptedOut.println("Exiting...");
        } else {
            interceptedOut.println("Unknown Command: " + input[0]);
        }

        return finalizeExecution(input, buffer);
    }

    private CommandResult finalizeExecution(String[] input, ByteArrayOutputStream buffer) {
        String outputStr = buffer.toString();
        CommandHistory hist = new CommandHistory(state, input, outputStr);
        history.push(hist);
        return new CommandResult(state, input, outputStr);
    }
}