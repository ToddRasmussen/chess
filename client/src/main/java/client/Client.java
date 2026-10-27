package client;

import java.util.Scanner;

import chess.ChessGame;
import client.commands.*;
import client.displays.Display;
import client.displays.TerminalDisplay;
import client.internal.ServerFacade;
import client.internal.State;

public class Client {
    // This Focuses on handling the 'large' scope and delegates the actual work
    private ServerFacade server;
    private CommandEngine commands;
    private Display display;

    public void run() {
        System.out.flush();
        System.out.println("Welcome to Chess. Type Help to get started.");
        server = new ServerFacade(8080);
        commands = new CommandEngine();
        display = new TerminalDisplay(server);

        //PRELOGIN
        commands.addCommand(new LoginCommand(server, display), State.PRELOGIN);
        commands.addCommand(new RegisterCommand(server, display), State.PRELOGIN);
        commands.addCommand(new ResetCommand(server, display), State.PRELOGIN);
        //POSTLOGIN
        commands.addCommand(new LogoutCommand(server, display), State.POSTLOGIN);
        commands.addCommand(new CreateCommand(server), State.POSTLOGIN);
        commands.addCommand(new ListCommand(server), State.POSTLOGIN);
        commands.addCommand(new JoinCommand(server, display), State.POSTLOGIN);
        commands.addCommand(new ObserveCommand(server, display), State.POSTLOGIN);

        // GAME - BLACK
        commands.addCommand(new LeaveCommand(server, display), State.BLACKTEAM);
        commands.addCommand(new MoveCommand(ChessGame.TeamColor.BLACK, display, server), State.BLACKTEAM);
        commands.addCommand(new MovesCommand(ChessGame.TeamColor.BLACK, server, display), State.BLACKTEAM);
        commands.addCommand(new RedrawCommand(server, display), State.BLACKTEAM);
        commands.addCommand(new ResignCommand(server), State.BLACKTEAM);

        // GAME - WHITE
        commands.addCommand(new LeaveCommand(server, display), State.WHITETEAM);
        commands.addCommand(new MoveCommand(ChessGame.TeamColor.WHITE, display, server), State.WHITETEAM);
        commands.addCommand(new MovesCommand(ChessGame.TeamColor.WHITE, server, display), State.WHITETEAM);
        commands.addCommand(new RedrawCommand(server, display), State.WHITETEAM);
        commands.addCommand(new ResignCommand(server), State.WHITETEAM);

        // GAME - OBSERVER
        commands.addCommand(new LeaveCommand(server, display), State.OBSERVER);
        commands.addCommand(new RedrawCommand(server, display), State.OBSERVER);

        display.init();
        while (!commands.getState().equals(State.OFF)) {
            display.tick(commands.getState());
            String[] input = display.getInput();
            if (input != null) {
                commands.process(input,display.getPrintStream());
            }
        }
        display.close();
    }
}
