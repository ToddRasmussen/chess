package service;

import org.junit.jupiter.api.Test;

public class CustomServiceTests {

    @Test
    public void testRegisterUserPositive() {
        //Tests Simply Registering a perfectly Valid User
    }

    @Test
    public void testRegisterUserAlreadyTakenNegative() {
        //Tests Registering a already taken username
    }

    @Test
    public void testLoginUserPositive() {
        //Tests Simply Logging in
    }

    @Test
    public void testLoginUserIncorrectPasswordNegative() {
        //Tests Incorrect Password
    }

    @Test
    public void testLoginUserUnknownUsernameNegative() {
        //Tests unregistered username
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