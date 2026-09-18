package dataaccess.database;

import dataaccess.ColorAlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.UnknownColorException;
import dataaccess.interfaces.GameDAO;
import model.GameData;

import java.util.Collection;

public class DatabaseGameDAO implements GameDAO {

    public DatabaseGameDAO() {

    }

    public Collection<GameData> listGames() {
        return null;
    }

    public GameData getGame(int gameID) {
        return null;
    }

    public void joinGame(int gameID, String playerColor, String username)
            throws ColorAlreadyTakenException, UnknownColorException, DataAccessException {

    }

    public int createGame(String gameName) {
        return 0;
    }

    public void reset() {

    }
}
