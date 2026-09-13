package dataaccess;

import java.util.UUID;

import chess.ChessGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
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

    public boolean isGame(String gameID) {
        return (getGame(gameID) != null);
    }

    public String getPlayer(String gameID, String playerColor) {
        if (playerColor.equals("White")) {
            return getGame(gameID).getWhiteUsername();
        } else {
            return getGame(gameID).getBlackUsername();
        }
    }

    public void joinGame(String gameID, String playerColor, String username) throws ColorAlreadyTakenException {
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