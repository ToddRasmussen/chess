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

    }

    public GameData getGame(int gameID) {

    }

    public void joinGame(int gameID, String playerColor, String username)
            throws ColorAlreadyTakenException, UnknownColorException, DataAccessException {

    }

    public int createGame(String gameName) {

    }

    public void reset() {

    }
}
