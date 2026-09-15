package service;

import java.util.Collection;
import dataaccess.AuthDAO;
import dataaccess.ColorAlreadyTakenException;
import dataaccess.GameDAO;
import dataaccess.UnknownColorException;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

public class Service {

    private AuthDAO authData;
    private UserDAO userData;
    private GameDAO gameData;

    public Service() {
        authData = new AuthDAO();
        userData = new UserDAO();
        gameData = new GameDAO();
    }

    public AuthData registerUser(UserData user) throws AlreadyTakenException, BadRequestException {
        if (user == null || user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Bad Request");
        }
        if (userData.isUser(user)) {
            throw new AlreadyTakenException("Username Already Taken");
        }
        userData.createUser(user);
        return authData.createAuth(user);
    }

    public AuthData loginUser(UserData user) throws DoesNotExistException, IncorrectPasswordException, BadRequestException {
        if (user == null || user.username() == null || user.password() == null) {
            throw new BadRequestException("Bad Request");
        }
        if (!userData.isUser(user)) {
            throw new DoesNotExistException("Unknown Username");
        }
        if (!userData.validatePassword(user)) {
            throw new IncorrectPasswordException("Incorrect Password");
        }
        return authData.createAuth(user);
    }

    private void checkAuth(String authToken) throws InvalidAuthorizationException {
        if (!authData.validAuth(authToken)) {
            throw new InvalidAuthorizationException("Unauthorized");
        }
    }

    public void logoutUser(String authToken) throws InvalidAuthorizationException {
        checkAuth(authToken);
        authData.deleteAuth(authToken);
    }

    public Collection<GameData> listGames(String authToken) throws InvalidAuthorizationException {
        checkAuth(authToken);
        return gameData.listGames();
    }

    public int createGame(String authToken, String gameName) throws InvalidAuthorizationException, BadRequestException {
        checkAuth(authToken);
        if (gameName == null || gameName.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        return gameData.createGame(gameName);
    }

    public void joinGame(String authToken, int gameID, String playerColor)
            throws InvalidAuthorizationException, ColorAlreadyTakenException, UnknownColorException, BadRequestException {
        checkAuth(authToken);
        String username = authData.getUser(authToken);
        gameData.joinGame(gameID, playerColor, username);
    }

    public void reset() {
        authData.reset();
        userData.reset();
        gameData.reset();
    }
}