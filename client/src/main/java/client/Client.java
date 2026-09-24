package client;

import chess.ChessGame;
import client.exceptions.InputException;
import client.exceptions.UnauthorizedException;
import model.UserData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private enum ClientState {
        PRELOGIN,
        POSTLOGIN,
        BLACKTEAM,
        WHITETEAM,
        OBSERVER,
        OFF
    }

    private ServerFacade server;
    private Client.ClientState state;
    private Scanner scanner = new Scanner(System.in);
    private Integer attachedGameID;

    public void run() {
        System.out.flush();
        System.out.println("Welcome to Chess. Type Help to get started.");
        state = Client.ClientState.PRELOGIN;
        server = new ServerFacade(8080);
        loop();
    }

    private void loop() {
        while (!state.equals(Client.ClientState.OFF)) {
            switch (state) {
                case Client.ClientState.PRELOGIN -> prelogin();
                case Client.ClientState.POSTLOGIN -> postlogin();
                case ClientState.BLACKTEAM -> blackTeam();
                case ClientState.WHITETEAM -> whiteTeam();
                case ClientState.OBSERVER -> observer();
            }
        }
    }

    private String[] getInput() {
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? new String[0] : line.split("\\s+");
    }

    private void prelogin() {
        System.out.print("[LOGGED OUT] >>> ");
        String[] input = getInput();
        switch (input[0].toLowerCase()) {
            case ("help") -> {
                displayHelp("register <USERNAME> <PASSWORD> <EMAIL>","to create an account");
                displayHelp("login <USERNAME> <PASSWORD>", "to play chess");
                displayHelp("quit", "playing chess");
                displayHelp("help","with possible commands");
            }
            case ("quit") -> state = ClientState.OFF;
            case ("login") -> {
                if (input.length < 3) {
                    System.out.println("Not Enough Inputs Given");
                    displayHelp("login <USERNAME> <PASSWORD>", "to play chess");
                    return;
                }
                try {
                    server.login(new UserData(input[1], input[2], ""));
                    state = ClientState.POSTLOGIN;
                } catch (UnauthorizedException e) {
                    System.out.println("Unable to Login, Incorrect Username or Password");
                } catch (InterruptedException | IOException e) {
                    System.out.println("Unable to Reach Server");
                } catch (Exception e) {
                    System.out.println("Unknown Server Error");
                }
            }
            case ("register") -> {
                if (input.length < 4) {
                    System.out.println("Not Enough Inputs Given");
                    displayHelp("register <USERNAME> <PASSWORD> <EMAIL>","to create an account");
                    return;
                }
                try {
                    server.register(new UserData(input[1], input[2], input[3]));
                } catch (UnauthorizedException e) {
                    System.out.println("Unable to Login, Incorrect Username or Password");
                }
            }
        }
    }

    private void postlogin() {

    }

    private void blackTeam() {

    }

    private void whiteTeam() {

    }

    private void observer() {

    }

    private void displayHelp(String command, String information) {
        String line = "\u001b[46]"+ command + "\u001b[49]" + " - " + information;
        System.out.println(line);
    }

    private void displayGameWhite(ChessGame game) {
        System.out.flush();
        //TODO
    }

    private void displayGameBlack(ChessGame game) {
        System.out.flush();
        //TODO
    }
}
