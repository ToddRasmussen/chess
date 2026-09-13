package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import model.AuthData;
import model.UserData;

public class CustomServiceTests {

    @Test
    public void testRegisterUserPositive() {
        //Tests Simply Registering a perfectly Valid User
        //Arrange
        Service service = new Service();
        String username = "player1";
        UserData user = new UserData(username, "password123", "p1@email.com");
        //Act
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        //Assert
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(username, auth.username());
        Assertions.assertNotNull(auth.authToken());
    }

    @Test
    public void testRegisterUserAlreadyTakenNegative() {
        //Tests Registering a already taken username
        //Arrange
        Service service = new Service();
        String username = "player1";
        UserData user1 = new UserData(username, "password123", "p1@email.com");
        UserData user2 = new UserData(username, "differentPass", "p2@email.com");
        //Act
        Assertions.assertDoesNotThrow(() -> service.registerUser(user1));
        //Assert
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            service.registerUser(user2);
        });
    }

    @Test
    public void testLoginUserPositive() {
        //Tests Simply Logging in
        //Arrange
        Service service = new Service();
        String username = "player1";
        UserData user = new UserData(username, "password123", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        //Act
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.loginUser(user));
        //Assert
        Assertions.assertNotNull(auth);
        Assertions.assertEquals(username, auth.username());
    }

    @Test
    public void testLoginUserIncorrectPasswordNegative() {
        //Tests Incorrect Password
        //Arrange
        Service service = new Service();
        String username = "player1";
        UserData user = new UserData(username, "password123", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        UserData badLogin = new UserData(username, "wrongPassword", null);
        //Act + Assert
        Assertions.assertThrows(IncorrectPasswordException.class, () -> {
            service.loginUser(badLogin);
        });
    }

    @Test
    public void testLoginUserUnknownUsernameNegative() {
        //Tests unregistered username
        //Arrange
        Service service = new Service();
        UserData badLogin = new UserData("fakeUser", "password123", null);
        //Act + Assert
        Assertions.assertThrows(DoesNotExistException.class, () -> {
            service.loginUser(badLogin);
        });
    }

    @Test
    public void testLogoutUserPositive() {
        //Tests Loging out User
    }

    @Test
    public void testLogoutUserInvalidAuthNegative() {
        //Tests Unknown authToken
    }

    @Test
    public void testListGamesPositive() {
        //Tests Listing Game
    }

    @Test
    public void testCreateGamePositive() {
        //Tests Creating Game
    }

    @Test
    public void testJoinGamePositive() {
        //Tests Joining Game
    }

    @Test
    public void testJoinGameTeamAlreadyTakenNegative() {
        //Tests Attempting to join team when already taken
    }

    @Test
    public void testJoinGameInvalidColorNegative() {
        //Tests Attempting to join team that does not exist
    }
}