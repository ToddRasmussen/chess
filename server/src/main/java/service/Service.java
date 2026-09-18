package service;

import java.util.Collection;

import dataaccess.*;
import dataaccess.interfaces.*;
import dataaccess.database.DatabaseAuthDAO;
import dataaccess.database.DatabaseGameDAO;
import dataaccess.database.DatabaseUserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

public class Service {

    private final AuthDAO authData;
    private final UserDAO userData;
    private final GameDAO gameData;

    public Service() throws Exception {
        authData = new DatabaseAuthDAO();
        userData = new DatabaseUserDAO();
        gameData = new DatabaseGameDAO();
    }

    public AuthData registerUser(UserData user) throws Exception {
        if (user == null || user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("Bad Request");
        }
        if (userData.isUser(user.username())) {
            throw new AlreadyTakenException("Username Already Taken");
        }
        userData.createUser(user);
        return authData.createAuth(user);
    }

    public AuthData loginUser(UserData user) throws Exception {
        if (user == null || user.username() == null || user.password() == null) {
            throw new BadRequestException("Bad Request");
        }
        if (!userData.isUser(user.username())) {
            throw new DoesNotExistException("Unknown Username");
        }
        if (!userData.validatePassword(user.username(), user.password())) {
            throw new IncorrectPasswordException("Incorrect Password");
        }
        return authData.createAuth(user);
    }

    private void checkAuth(String authToken) throws Exception {
        if (!authData.validAuth(authToken)) {
            throw new InvalidAuthorizationException("Unauthorized");
        }
    }

    public void logoutUser(String authToken) throws Exception {
        checkAuth(authToken);
        authData.deleteAuth(authToken);
    }

    public Collection<GameData> listGames(String authToken) throws Exception {
        checkAuth(authToken);
        return gameData.listGames();
    }

    public int createGame(String authToken, String gameName) throws Exception {
        checkAuth(authToken);
        if (gameName == null || gameName.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        return gameData.createGame(gameName);
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws Exception {
        checkAuth(authToken);
        String username = authData.getUser(authToken);
        gameData.joinGame(gameID, playerColor, username);
    }

    public void reset() throws Exception {
        authData.reset();
        userData.reset();
        gameData.reset();
    }
}