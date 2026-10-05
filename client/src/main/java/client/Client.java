package client;

import java.util.Scanner;

import chess.ChessGame;
import client.commands.*;
import client.displays.InlineDisplay;
import client.internal.State;

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
        display = new InlineDisplay(5);

        //PRELOGIN
        commands.addCommand(new LoginCommand(server), State.PRELOGIN);
        commands.addCommand(new RegisterCommand(server), State.PRELOGIN);
        commands.addCommand(new ResetCommand(server), State.PRELOGIN);
        //POSTLOGIN
        commands.addCommand(new LogoutCommand(server), State.POSTLOGIN);
        commands.addCommand(new CreateCommand(server), State.POSTLOGIN);
        commands.addCommand(new ListCommand(), State.POSTLOGIN);
        commands.addCommand(new JoinCommand(server), State.POSTLOGIN);
        commands.addCommand(new ObserveCommand(server), State.POSTLOGIN);

        //GAME
        commands.addCommand(new LeaveCommand(State.BLACKTEAM), State.BLACKTEAM);
        commands.addCommand(new MoveCommand(ChessGame.TeamColor.BLACK, display), State.BLACKTEAM);
        commands.addCommand(new MovesCommand(ChessGame.TeamColor.BLACK,display), State.BLACKTEAM);
        commands.addCommand(new RedrawCommand(display), State.BLACKTEAM);
        commands.addCommand(new ResignCommand(server), State.BLACKTEAM);

        commands.addCommand(new LeaveCommand(State.WHITETEAM), State.WHITETEAM);
        commands.addCommand(new MoveCommand(ChessGame.TeamColor.WHITE, display), State.WHITETEAM);
        commands.addCommand(new MovesCommand(ChessGame.TeamColor.WHITE, display), State.WHITETEAM);
        commands.addCommand(new RedrawCommand(display), State.WHITETEAM);
        commands.addCommand(new ResignCommand(server), State.WHITETEAM);

        commands.addCommand(new LeaveCommand(State.OBSERVER), State.OBSERVER);
        commands.addCommand(new RedrawCommand(display), State.OBSERVER);

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
        commands.process(getInput());
    }
}
