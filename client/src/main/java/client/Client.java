package client;

import java.util.Scanner;

public class Client {

    private ServerFacade server;
    private Scanner scanner = new Scanner(System.in);
    private Integer attachedGameID;
    private CommandLine commands;

    public void run() {
        System.out.flush();
        System.out.println("Welcome to Chess. Type Help to get started.");
        server = new ServerFacade(8080);
        commands = new CommandLine();
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
        commands.process(getInput());
    }
}
