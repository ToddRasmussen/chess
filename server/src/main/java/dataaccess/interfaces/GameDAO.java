package dataaccess.interfaces;

import java.util.Collection;
import model.GameData;
import dataaccess.ColorAlreadyTakenException;
import dataaccess.UnknownColorException;
import dataaccess.DataAccessException;

public interface GameDAO {

    public Collection<GameData> listGames() throws Exception ;

    public GameData getGame(int gameID) throws Exception ;

    public void joinGame(int gameID, String playerColor, String username) throws Exception ;

    public int createGame(String gameName) throws Exception ;

    public void reset() throws Exception ;
}