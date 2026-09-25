package client;

import client.Interfaces.CommandLine;

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
        commands = createCommands();
        attachedGameID = null;
        while (!commands.getState().equals(State.OFF)) {
            loop();
        }
    }

    private CommandLine createCommands() {
        CommandLine commands = new CommandLine();


        return commands;
    }


    private void loop() {
        System.out.print("[" + commands.getState().toString() + "] >>> ");


    }
}
