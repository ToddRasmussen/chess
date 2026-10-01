package client;

import java.util.Scanner;

import client.commands.*;
import client.displays.TerminalDisplay;

public class Client {
    // This Focuses on handling the 'large' scope and delegates the actual work
    private ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);
    private CommandLine commands;
    private Display display;

    public void run() {
        System.out.flush();
        System.out.println("Welcome to Chess. Type Help to get started.");
        server = new ServerFacade(8080);
        commands = new CommandLine();
        display = new TerminalDisplay(5);

        //PRELOGIN
        commands.addCommand(new LoginCommand(), State.PRELOGIN);
        commands.addCommand(new RegisterCommand(), State.PRELOGIN);
        commands.addCommand(new ResetCommand(), State.PRELOGIN);
        //POSTLOGIN
        commands.addCommand(new LogoutCommand(), State.POSTLOGIN);
        commands.addCommand(new CreateCommand(), State.POSTLOGIN);
        commands.addCommand(new ListCommand(), State.POSTLOGIN);
        commands.addCommand(new JoinCommand(), State.POSTLOGIN);
        commands.addCommand(new ObserveCommand(), State.POSTLOGIN);

        //GAME
        commands.addCommand(new ExitCommand(), State.BLACKTEAM);
        commands.addCommand(new ExitCommand(), State.WHITETEAM);
        commands.addCommand(new ExitCommand(), State.OBSERVER);

        while (!commands.getState().equals(State.OFF)) {
            loop();
        }
    }

    private String[] getInput() {
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? new String[0] : line.split("\\s+");
    }

    private void loop() {
        display.display(commands.getAttachedGameID(), server, commands.getState());
        System.out.print("[" + commands.getState().toString() + "] >>> ");
        commands.process(getInput(), server);
    }
}
