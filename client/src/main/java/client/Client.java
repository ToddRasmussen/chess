package client;

import client.exceptions.InputException;
import client.exceptions.UnauthorizedException;
import model.GameData;
import model.UserData;

import java.util.Collection;
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
            try {
                switch (state) {
                    case ClientState.PRELOGIN -> prelogin();
                    case ClientState.POSTLOGIN -> postlogin();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private String[] getInput() {
        String line = scanner.nextLine().trim();
        return line.isEmpty() ? new String[0] : line.split("\\s+");
    }

    private void prelogin() throws Exception {
        String[] input = getInput();
        switch (input[0].toLowerCase()) {
            case "help" -> displayHelp();
            case "quit" -> state = ClientState.OFF;
            case "login" -> {
                if (input.length < 3) {
                    throw new InputException("Not Enough Inputs Given");
                }
                    loginUser(input[1], input[2]);
            }
            case "register" -> {
                if (input.length < 4) {
                    throw new InputException("Not Enough Inputs Given");
                }
                registerUser(input[1], input[2], input[3]);
            }
        }
    }

    private void postlogin() throws Exception {
        String[] input = getInput();
        switch (input[0]) {
            case "help" -> displayHelp();
            case "quit" -> {
                logoutUser();
                state = ClientState.OFF;
            }
            case "logout" -> logoutUser();
            case "create" -> {
                if (input.length < 2) {
                    throw new InputException("Not Enough Inputs Given");
                }
                createGame(input[1]);
            }
            case "list" -> listGames();
            case "join" -> {
                if (input.length < 3) {
                    throw new InputException("Not Enough Inputs Given");
                }
                joinGame(Integer.getInteger(input[1]), input[2]);
            }
            case "observe" -> {
                if (input.length < 2) {
                    throw new InputException("Not Enough Inputs Given");
                }
                observeGame(Integer.getInteger(input[1]));
            }
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

    private void loginUser(String username, String password) throws Exception {
        try {
            server.login(new UserData(username, password, ""));
            state = ClientState.POSTLOGIN;

        } catch (UnauthorizedException e) {
            System.out.println("Invalid username and/or password");
        }
    }

    private void registerUser(String username, String password, String email) throws Exception {
        server.register(new UserData(username, password, email));
    }

    private void logoutUser() throws Exception {
        server.logout();
    }

    private void createGame(String gameName) throws Exception {
        int gameID = server.newGame(gameName);
        System.out.println("Created New Game with ID: " + gameID);
    }

    private void listGames() throws Exception {
        Collection<GameData> games = server.games();
        System.out.println("Games:");
        System.out.println(games.toString());
    }

    private void joinGame(int gameID, String playerColor) throws Exception {
        server.joinGame(gameID, playerColor);
    }

    private void observeGame(int gameID) {
        //TODO
    }

}
