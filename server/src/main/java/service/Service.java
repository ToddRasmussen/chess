



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

}