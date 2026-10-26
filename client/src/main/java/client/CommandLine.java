package client;

import client.commands.Command;
import client.internal.State;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

public class CommandLine {
    // This Focuses on taking an input and processing it
    private State state;
    private final Map<State, Collection<Command>> commands;
    private final Deque<CommandHistory> history;

    public record CommandHistory(State state, String[] inputs, String output) {}

    public CommandLine() {
        commands = new HashMap<>();
        state = State.PRELOGIN;
        history = new ArrayDeque<>();
    }

    public void addCommand(Command newCommand, State state) {
        getCommands(state).add(newCommand);
    }

    private Collection<Command> getCommands(State state) {
        if (!commands.containsKey(state)) {
            commands.put(state, new LinkedList<>());
        }
        return commands.get(state);
    }

    public String prefix(State state) {
        return "[" + state.toString() + "] >>> ";
    }

    public String prefix() {
        return prefix(state);
    }

    public State getState() {
        return state;
    }

    public void displayHelp(PrintStream out) {
        new Command("help", "with chess").displayHelp(out);
        Collection<Command> commands = getCommands(state);
        for(Command command : commands) {
            command.displayHelp(out);
        }
        new Command("quit", "chess").displayHelp(out);
    }

    public void process(String[] input, PrintStream out) {
        if (input.length < 1) {
            return;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream wrappedOut = new PrintStream(new OutputStream() {
            public void write(int b) {
                buffer.write(b);
                out.write(b);
            }
            public void write(@NotNull byte[] b, int off, int len) {
                buffer.write(b, off, len);
                out.write(b, off, len);
            }
            public void flush() throws IOException {
                buffer.flush();
                out.flush();
            }
        });

        for (Command command : getCommands(state)) {
            if (!command.isCommand(input[0])) {
                continue;
            }
            if (!command.validateInput(input)) {
                wrappedOut.println("Invalid Input");
                command.displayHelp(wrappedOut);
                recordHistory(input, buffer);
                return;
            }
            try {
                State newState = command.run(input, wrappedOut);
                if (newState != null) {
                    state = newState;
                }
            } catch (Exception e) {
                wrappedOut.println("Unexpected Error: " + e);
            }
            recordHistory(input, buffer);
            return;
        }

        if ("help".equals(input[0])) {
            displayHelp(wrappedOut);
        } else if ("quit".equals(input[0])) {
            state = State.OFF;
        } else {
            wrappedOut.println("Unknown Command: " + input[0]);
        }
        recordHistory(input, buffer);
    }

    private void recordHistory(String[] input, ByteArrayOutputStream buffer) {
        String outputStr = buffer.toString();
        history.push(new CommandHistory(state, input, outputStr));
    }

}