



public class Service {

    private AuthDAO authData;
    private UserDAO userData;
    private GameDAO gameData;

    public Service() {
        authData = new AuthDAO();
        userData = new UserDAO();
        gameData = new GameDAO();
    }

    private record Message(String message) {}


    public Result registerUser(UserData user) {
        UserData existingUser = userData.getUser(user.username());
        if (existingUser != null) {
            return new Result(403, new Message("Error: Username Already Taken"));
        }
        userData.createUser(user);
        AuthData auth = authData.createAuth(user);
        return new Result(200, auth);
    }

    public Result loginUser(UserData user) {
        UserData existingUser = userData.getUser(user.username());
        if (existingUser == null) {
            return new Result(403, new Message("Error: Unknown Username"));
        }
        if (existingUser.password() != user.password()) {
            return new Result(403, new Message("Error: Incorrect Password"));
        }
        AuthData auth = authData.createAuth(user);
        return new Result(200, auth);
    }

    private Result checkAuth(String authToken) {
        if (!authData.validAuth(authToken)) {
            return new Result(401, new Message("Error: Unauthorized"));
        }
        return null;
    }

    public Result logoutUser(String authToken) {
        Result out = checkAuth(authToken);
        if (out != null) {
            return out;
        }
        authData.deleteAuth(authToken);
        return new Result(200, null);
    }

    public Result listGames(String authToken) {
        Result out = checkAuth(authToken);
        if (out != null) {
            return out;
        }
        Collection<GameData> games = gameData.listGames();
        return new Result(200, games);
    }

    public Result createGame(String authToken, String gameName) {
        Result out = checkAuth(authToken);
        if (out != null) {
            return out;
        }
        String gameID = GameData.createGame();
        return new Result(200, gameID);
    }

    public Result joinGame(String authToken, String gameID) {
        Result out = checkAuth(authToken);
        if (out != null) {
            return out;
        }
        
    }

}