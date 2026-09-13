package dataaccess;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import chess.ChessGame;
import model.GameData;

public class GameDAO {

    private Map<String, GameData> games;

    public GameDAO() {
        this.games = new HashMap<>();
    }

    private String generateID() {
        return UUID.randomUUID().toString();
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public GameData getGame(String gameID) {
        return games.get(gameID);
    }


    public void joinGame(String gameID, String playerColor, String username) throws ColorAlreadyTakenException, UnknownColorException {
        GameData game = getGame(gameID);
        if (playerColor.equals("White")) {
            if (game.getWhiteUsername() == null || game.getWhiteUsername().isEmpty()) {
                game.setWhiteUsername(username);
            } else {
                throw new ColorAlreadyTakenException("White is already taken");
            }
        } else if (playerColor.equals("Black")) {
            if (game.getWhiteUsername() == null || game.getBlackUsername().isEmpty()) {
                game.setBlackUsername(username);
            } else {
                throw new ColorAlreadyTakenException("Black is already taken");
            }
        } else {
            throw new UnknownColorException("Unknown Color");
        }
    }

    public String addGame(GameData game) {
        String gameID = generateID();
        games.put(gameID, game);
        return gameID;
    }

    public String createGame(String gameName) {
        GameData newGame = new GameData(gameName, new ChessGame());
        String gameID = addGame(newGame);
        return gameID;
    }


    public void reset() {
        this.games = new HashMap<>();
    }

}