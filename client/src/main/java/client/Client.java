package client;

import model.UserData;

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


    private String[] getInput() {
        return scanner.nextLine().split("\\s+");
    }

    private void prelogin() {
        String[] input = getInput();
        switch (input[0]) {
            case "help": displayHelp();
            case "quit": quit();
            case "login": loginUser(input[1], input[2]);
            case "register": registerUser();
        }
    }

    private void postlogin() {
        String[] input = getInput();
        switch (input[0]) {
            case "help": displayHelp();
            case "quit": quit();
            case "logout": logoutUser();
            case "create": createGame();
            case "list": listGames();
            case "join": joinGame();
            case "observe": observeGame();
        }
    }

    private void displayHelp() {
        Map<String, String> commands = new HashMap<>();
        switch (state) {
            case ClientState.PRELOGIN -> {
                commands.put("register <USERNAME> <PASSWORD> <EMAIL>","to create an account");
                commands.put("login <USERNAME> <PASSWORD>", "to play chess");
            }
            case ClientState.POSTLOGIN -> {
                commands.put("create <NAME>", "a game");
                commands.put("list", "games");
                commands.put("join <ID> [WHITE|BLACK]", "a game");
                commands.put("observe <ID>", "a game");
                commands.put("logout", "when you are done");
            }
        }
        commands.put("quit", "playing chess");
        commands.put("help","with possible commands");

        for (Map.Entry<String, String> entry : commands.entrySet()) {
            String line = "\u001b[46]"+entry.getKey() + "\u001b[49]" + " - " + entry.getValue();
            System.out.println(line);
        }
    }
    private void quit() {
        if (state.equals(ClientState.POSTLOGIN)) {
            logoutUser();
        }
        state = ClientState.OFF;
    }

    private void loginUser(String username, String password) {
        try {
            server.login(new UserData(username, password, ""));

        } catch Exception {

        }
    }
}
