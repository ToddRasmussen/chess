package service;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dataaccess.ColorAlreadyTakenException;
import dataaccess.UnknownColorException;
import model.AuthData;
import model.GameData;
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
        //Arrange
        Service service = new Service();
        UserData user = new UserData("player1", "password123", "p1@email.com");
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        //Act
        Assertions.assertDoesNotThrow(() -> {
            service.logoutUser(auth.authToken());
        });
        //Assert
        Assertions.assertThrows(InvalidAuthorizationException.class, () -> {
            service.logoutUser(auth.authToken());
        });
    }
    @Test
    public void testLogoutUserInvalidAuthNegative() {
        //Tests Unknown authToken
        //Arrange
        Service service = new Service();
        //Act + Assert
        Assertions.assertThrows(InvalidAuthorizationException.class, () -> {
            service.logoutUser("bogus-auth-token-999");
        });
    }
    @Test
    public void testListGamesPositive() {
        //Tests Listing Game
        //Arrange
        Service service = new Service();
        UserData user = new UserData("player1", "password123", "p1@email.com");
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        //Act
        Collection<GameData> games = Assertions.assertDoesNotThrow(() -> service.listGames(auth.authToken()));
        //Assert
        Assertions.assertNotNull(games);
    }
    @Test
    public void testCreateGamePositive() {
        //Tests Creating Game
        //Arrange
        Service service = new Service();
        UserData user = new UserData("player1", "password123", "p1@email.com");
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        //Act
        int gameID = Assertions.assertDoesNotThrow(() -> service.createGame(auth.authToken(), "Epic Chess Game"));
        //Assert
        Assertions.assertFalse(gameID == 0);
    }
    @Test
    public void testJoinGamePositive() {
        //Tests Joining Game
        //Arrange
        Service service = new Service();
        UserData user = new UserData("player1", "password123", "p1@email.com");
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        int gameID = Assertions.assertDoesNotThrow(() -> service.createGame(auth.authToken(), "Epic Chess Game"));
        //Act + Assert
        Assertions.assertDoesNotThrow(() -> {
            service.joinGame(auth.authToken(), gameID, "BLACK");
        });
    }
    @Test
    public void testJoinGameTeamAlreadyTakenNegative() {
        //Tests Attempting to join team when already taken
        //Arrange
        Service service = new Service();
        UserData user1 = new UserData("player1", "password123", "p1@email.com");
        UserData user2 = new UserData("player2", "password123", "p2@email.com");
        AuthData auth1 = Assertions.assertDoesNotThrow(() -> service.registerUser(user1));
        AuthData auth2 = Assertions.assertDoesNotThrow(() -> service.registerUser(user2));
        int gameID = Assertions.assertDoesNotThrow(() -> service.createGame(auth1.authToken(), "Epic Chess Game"));
        // Player 1 claims White
        Assertions.assertDoesNotThrow(() -> service.joinGame(auth1.authToken(), gameID, "WHITE"));
        //Act + Assert
        // Player 2 tries to claim White and should throw a ColorAlreadyTakenException since Player 1 already claimed White
        Assertions.assertThrows(ColorAlreadyTakenException.class, () -> {
            service.joinGame(auth2.authToken(), gameID, "WHITE");
        });
    }
    @Test
    public void testJoinGameInvalidColorNegative() {
        //Tests Attempting to join team that does not exist
        //Arrange
        Service service = new Service();
        UserData user = new UserData("player1", "password123", "p1@email.com");
        AuthData auth = Assertions.assertDoesNotThrow(() -> service.registerUser(user));
        int gameID = Assertions.assertDoesNotThrow(() -> service.createGame(auth.authToken(), "Epic Chess Game"));
        //Act + Assert
        Assertions.assertThrows(UnknownColorException.class, () -> {
            service.joinGame(auth.authToken(), gameID, "Red");
        });
    }
}