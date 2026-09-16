package dataaccess.memory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import chess.ChessGame;
import dataaccess.ColorAlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.UnknownColorException;
import dataaccess.interfaces.GameDAO;
import model.GameData;

public class MemoryGameDAO implements GameDAO {

    private Map<Integer, GameData> games;
    private int nextGameID;

    public MemoryGameDAO() {
        this.games = new HashMap<>();
        nextGameID = 1;
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }


    public void joinGame(int gameID, String playerColor, String username)
        throws ColorAlreadyTakenException, UnknownColorException, DataAccessException
    {
        GameData game = getGame(gameID);
        if (game == null) {
            throw new DataAccessException("No Game with given ID");
        }

        if ("WHITE".equals(playerColor)) {
            if (game.getWhiteUsername() == null || game.getWhiteUsername().isEmpty()) {
                game.setWhiteUsername(username);
            } else {
                throw new ColorAlreadyTakenException("White is already taken");
            }
        } else if ("BLACK".equals(playerColor)) {
            if (game.getBlackUsername() == null || game.getBlackUsername().isEmpty()) {
                game.setBlackUsername(username);
            } else {
                throw new ColorAlreadyTakenException("Black is already taken");
            }
        } else {
            throw new UnknownColorException("Unknown Color");
        }
    }

    public int createGame(String gameName) {
        int gameID = nextGameID++;
        GameData newGame = new GameData(gameID, gameName, new ChessGame());
        games.put(gameID, newGame);
        return gameID;
    }

    public void reset() {
        this.games = new HashMap<>();
    }
}