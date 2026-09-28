package client;

import java.util.Scanner;
import client.Commands.LoginCommand;
import client.Commands.RegisterCommand;

public class Client {
    // This Focuses on handling the 'large' scope and delegates the actual work
    private ServerFacade server;
    private final Scanner scanner = new Scanner(System.in);
    private Integer attachedGameID;
    private CommandLine commands;

    public void run() {
        System.out.flush();
        System.out.println("Welcome to Chess. Type Help to get started.");
        server = new ServerFacade(8080);
        commands = new CommandLine();

        commands.addCommand(new LoginCommand(), State.PRELOGIN);
        commands.addCommand(new RegisterCommand(), State.PRELOGIN);

        attachedGameID = null;
        while (!commands.getState().equals(State.OFF)) {
            loop();
        }
    }

    private String[] getInput() {
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? new String[0] : line.split("\\s+");
    }

    private void loop() {
        System.out.print("[" + commands.getState().toString() + "] >>> ");
        commands.process(getInput(), server);
    }
}
