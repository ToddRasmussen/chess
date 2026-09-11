



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


    public AuthData registerUser(UserData user)  throws AlreadyTakenException {
        if (userData.isUser(user)) {
            throw new AlreadyTakenException("Username Already Taken");
        }
        userData.createUser(user);
        return authData.createAuth(user);
    }

    public AuthData loginUser(UserData user) throws DoesNotExistException, IncorrectPasswordException {
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
            throw new InvalidAuthorizationException("Error: Unauthorized");
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

    public String createGame(String authToken, String gameName) throws InvalidAuthorizationException {
        checkAuth(authToken);
        return GameData.createGame();
    }

    public Result joinGame(String authToken, String gameID) throws InvalidAuthorizationException {
        checkAuth(authToken);
        //TODO
    }

}