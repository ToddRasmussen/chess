package service;

import java.util.Collection;

import chess.ChessGame;
import chess.ChessMove;
import dataaccess.interfaces.*;
import dataaccess.sql.DatabaseAuthDAO;
import dataaccess.sql.DatabaseGameDAO;
import dataaccess.sql.DatabaseUserDAO;
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


    public String getPlayer(String authToken) throws Exception {
        return authData.getUser(authToken);
    }

    public ChessGame.TeamColor getTeam(Integer gameID, String player) throws Exception {
        GameData game = gameData.getGame(gameID);
        if (game.getBlackUsername() != null && game.getBlackUsername().equals(player)) {
            return ChessGame.TeamColor.BLACK;
        } else if (game.getWhiteUsername() != null && game.getWhiteUsername().equals(player)) {
            return ChessGame.TeamColor.WHITE;
        } else {
            return null;
        }
    }

    public ChessGame makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        checkAuth(authToken);
        String username = authData.getUser(authToken);
        GameData gameDataObj = gameData.getGame(gameID);
        if (gameDataObj == null) {
            throw new Exception("Game not found");
        }

        ChessGame game = gameDataObj.getGame();

        if (game.getResults().isFinished()) {
            throw new Exception("Game is over");
        }

        ChessGame.TeamColor team = getTeam(gameID, username);
        if (team == null || team != game.getTeamTurn()) {
            throw new Exception("Not your turn or you are a spectator");
        }

        game.makeMove(move);
        gameData.updateGame(gameDataObj);

        return game;
    }

    public ChessGame resignGame(String authToken, int gameID) throws Exception {
        checkAuth(authToken);
        String username = authData.getUser(authToken);
        GameData gameDataObj = gameData.getGame(gameID);
        if (gameDataObj == null) {
            throw new Exception("Game not found");
        }

        ChessGame.TeamColor team = getTeam(gameID, username);
        if (team == null) {
            throw new Exception("Spectators cannot resign");
        }

        ChessGame game = gameDataObj.getGame();

        if (game.getResults().isFinished()) {
            throw new Exception("Game is already over");
        }

        game.resign(team);
        gameData.updateGame(gameDataObj);

        return game;
    }

    public ChessGame getChessGame(int gameID) throws Exception {
        GameData game = gameData.getGame(gameID);
        if (game == null) {
            throw new Exception("Game not found");
        }
        return game.getGame();
    }

    public void leaveGame(String authToken, int gameID) throws Exception {
        checkAuth(authToken);
        String username = authData.getUser(authToken);
        GameData game = gameData.getGame(gameID);
        if (game == null) {
            return;
        }

        boolean updated = false;
        if (username.equals(game.getWhiteUsername())) {
            game.setWhiteUsername(null);
            updated = true;
        } else if (username.equals(game.getBlackUsername())) {
            game.setBlackUsername(null);
            updated = true;
        }

        if (updated) {
            gameData.updateGame(game);
        }
    }

    public void reset() throws Exception {
        authData.reset();
        userData.reset();
        gameData.reset();
    }
}