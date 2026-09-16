package dataaccess.interfaces;

import java.util.Collection;
import model.GameData;
import dataaccess.ColorAlreadyTakenException;
import dataaccess.UnknownColorException;
import dataaccess.DataAccessException;

public interface GameDAO {

    public Collection<GameData> listGames();

    public GameData getGame(int gameID);

    public void joinGame(int gameID, String playerColor, String username)
        throws ColorAlreadyTakenException, UnknownColorException, DataAccessException;

    public int createGame(String gameName);

    public void reset();
}