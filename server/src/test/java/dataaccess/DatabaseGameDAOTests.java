package dataaccess;

import dataaccess.sql.DatabaseAuthDAO;
import dataaccess.sql.DatabaseGameDAO;
import dataaccess.interfaces.GameDAO;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class DatabaseGameDAOTests {
    @BeforeEach
    public void setUp() {
        Assertions.assertDoesNotThrow(() -> new DatabaseAuthDAO().reset());
    }

    @Test
    public void testListGames() {
        //Arrange
        GameDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseGameDAO());
        Assertions.assertDoesNotThrow(() -> dao.createGame("test game"));
        //Act
        Collection<GameData> games = Assertions.assertDoesNotThrow(() -> dao.listGames());
        //Assert
        Assertions.assertFalse(games.isEmpty());
    }

    @Test
    public void testGetGame() {
        //Arrange
        GameDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseGameDAO());
        int gameID = Assertions.assertDoesNotThrow(() -> dao.createGame("test game"));
        //Act
        GameData game = Assertions.assertDoesNotThrow(() -> dao.getGame(gameID));
        //Assert
        Assertions.assertNotNull(game);
    }

    @Test
    public void testJoinGameWhite() {
        //Arrange
        GameDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseGameDAO());
        int gameID = Assertions.assertDoesNotThrow(() -> dao.createGame("test game"));
        //Act + Assert
        Assertions.assertDoesNotThrow(() -> dao.joinGame(gameID, "WHITE", "player"));
    }

    @Test
    public void testJoinGameBlack() {
        //Arrange
        GameDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseGameDAO());
        int gameID = Assertions.assertDoesNotThrow(() -> dao.createGame("test game"));
        //Act + Assert
        Assertions.assertDoesNotThrow(() -> dao.joinGame(gameID, "BLACK", "player"));
    }

    @Test
    public void testCreateGame() {
        //Arrange
        GameDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseGameDAO());
        //Act + Assert
        int gameID = Assertions.assertDoesNotThrow(() -> dao.createGame("test game"));
    }
}
