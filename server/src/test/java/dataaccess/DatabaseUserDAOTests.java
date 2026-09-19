package dataaccess;

import dataaccess.database.DatabaseUserDAO;
import dataaccess.interfaces.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatabaseUserDAOTests {

    @BeforeEach
    public void setUp() {
        Assertions.assertDoesNotThrow(() -> new DatabaseUserDAO().reset());
    }

    @Test
    public void testCreateUser() {
        //Arrange
        UserDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseUserDAO());
        UserData user = new UserData("user","password","example@email.com");
        //Act + Assert
        Assertions.assertDoesNotThrow(() -> dao.createUser(user));
    }

    @Test
    public void testGetUser() {
        //Arrange
        UserDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseUserDAO());
        String username = "user";
        UserData user = new UserData(username,"password","example@email.com");
        Assertions.assertDoesNotThrow(() -> dao.createUser(user));
        //Act
        UserData returnedUser = Assertions.assertDoesNotThrow(() -> dao.getUser(username));
        //Assert
        Assertions.assertEquals(user.username(),returnedUser.username());
    }

    @Test
    public void testIsUser() {
        //Arrange
        UserDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseUserDAO());
        String username = "user";
        UserData user = new UserData(username,"password","example@email.com");
        Assertions.assertDoesNotThrow(() -> dao.createUser(user));
        //Act
        boolean condition = Assertions.assertDoesNotThrow(() -> dao.isUser(username));
        //Assert
        Assertions.assertTrue(condition);
    }

    @Test
    public void testValidatePassword() {
        //Arrange
        UserDAO dao = Assertions.assertDoesNotThrow(() -> new DatabaseUserDAO());
        String username = "user";
        String password = "password";
        UserData user = new UserData(username,password,"example@email.com");
        Assertions.assertDoesNotThrow(() -> dao.createUser(user));
        //Act
        boolean condition = Assertions.assertDoesNotThrow(() -> dao.validatePassword(username,password));
        //Assert
        Assertions.assertTrue(condition);
    }

}
