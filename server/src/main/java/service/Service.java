



public class Service {

    private AuthDAO authData;
    private UserDAO userData;
    private GameDAO gameData;

    public Service() {
        authData = new AuthDAO();
        userData = new UserDAO();
        gameData = new GameDAO();
    }



    public Result registerUser(UserData user) {
        UserData existingUser = userData.getUser(user.username());
        if (existingUser != null) {
            return;
        }
        userData.registerUser(user);
        authData.createAuth(user);
    }

    public Result loginUser(UserData user) {
        UserData existingUser = userData.getUser(user.username());
        if (existingUser == null) {
            return;
        }
        if (existingUser.password() != user.password()) {
            return;
        }
        authData.createAuth(user);
    }

    private Result checkAuth(AuthData auth) {
        if (!authData.validAuth(auth)) {
            return;
        }
    }

    public Result logoutUser(AuthData auth) {
        Result out = checkAuth(auth);
        if (out != null) {
            return out;
        }
        authData.deleteAuth(auth);
    }

    public Result listGames(AuthData auth) {
        Result out = checkAuth(auth);
        if (out != null) {
            return out;
        }
        Collection<GameData> games = gameData.listGames()
    }

    public Result joinGame(AuthData auth, String gameID) {
        Result out = checkAuth(auth);
        if (out != null) {
            return out;
        }
        
    }

}