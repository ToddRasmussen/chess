package dataaccess;

import model.AuthData;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import dataaccess.interfaces.AuthDAO;
import dataaccess.database.DatabaseAuthDAO;
import org.junit.jupiter.api.Test;

public class DatabaseAuthDAOTests {
    @BeforeEach
    public void setUp() {
        Assertions.assertDoesNotThrow(() -> new DatabaseAuthDAO().reset());
    }
    @Test
    public void testCreateAuth() {
        //Arrange
        UserData user = new UserData("newUser","password","example@email.com");
        AuthDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseAuthDAO());
        //Act
        AuthData auth = Assertions.assertDoesNotThrow(() -> dao.createAuth(user));
        //Assert
        Assertions.assertEquals(user.username(),auth.username());
    }

    @Test
    public void testGetUser() {
        //Arrange
        UserData user = new UserData("newUser","password","example@email.com");
        AuthDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseAuthDAO());
        AuthData auth = Assertions.assertDoesNotThrow(() -> dao.createAuth(user));
        //Act
        String username = Assertions.assertDoesNotThrow(() -> dao.getUser(auth.authToken()));
        //Assert
        Assertions.assertEquals(username, user.username());
    }

    @Test
    public void testValidAuth() {
        //Arrange
        UserData user = new UserData("newUser","password","example@email.com");
        AuthDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseAuthDAO());
        AuthData auth = Assertions.assertDoesNotThrow(() -> dao.createAuth(user));
        //Act
        boolean validity = Assertions.assertDoesNotThrow(() -> dao.validAuth(auth.authToken()));
        // Assert
        Assertions.assertTrue(validity);
    }

    @Test
    public void testDeleteAuth() {
        //Arrange
        UserData user = new UserData("newUser","password","example@email.com");
        AuthDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseAuthDAO());
        AuthData auth = Assertions.assertDoesNotThrow(() -> dao.createAuth(user));
        //Act
        Assertions.assertDoesNotThrow(() -> dao.deleteAuth(auth.authToken()));
        // Assert
        Assertions.assertFalse(Assertions.assertDoesNotThrow(() -> dao.validAuth(auth.authToken())));
    }

}
