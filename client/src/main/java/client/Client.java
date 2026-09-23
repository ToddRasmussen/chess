package client;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private enum ClientState {
        PRELOGIN,
        POSTLOGIN,
        OFF
    }
    private ServerFacade server;
    private ClientState state;
    private Scanner scanner = new Scanner(System.in);

    public void run() {
        System.out.flush();
        System.out.println("Welcome to Chess. Type Help to get started.");
        state = ClientState.PRELOGIN;
        server = new ServerFacade(8080);
        loop();
    }

    private void loop() {
        while (!state.equals(ClientState.OFF)) {
            switch (state) {
                case ClientState.PRELOGIN: prelogin();
                case ClientState.POSTLOGIN: postlogin();
            }
        }
    }


    private String getInput() {
        return scanner.nextLine();
    }

    private void prelogin() {
        String input = getInput();
        switch (input) {
            case "help": displayHelp();
            case "quit": state = ClientState.OFF;
            case "login": loginUser();
            case "register": registerUser();
        }
    }

    private void postlogin() {
        String input = getInput();
        switch (input) {
            case "help": displayHelp();
            case "logout": logoutUser();
            case "create": createGame();
            case "list": listGames();
            case "join": joinGame();
            case "observe": observeGame();
            case "quit": {
                logoutUser();
                state = ClientState.OFF;
            }
        }
    }


    private void displayHelp() {
        Map<String, String> commands = new HashMap<>();
        switch (state) {
            case ClientState.PRELOGIN -> {

            }
            case ClientState.POSTLOGIN -> {
                
            }
        }
    }
}
