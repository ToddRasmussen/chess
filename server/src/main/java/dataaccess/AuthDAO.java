
import java.util.UUID;


public class AuthDAO {

    private Map<String, String> sessions;

    public AuthDAO() {
        this.sessions = new HashMap<>();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(UserData user) {
        AuthData auth = new AuthData(generateToken(), user.username());
        sessions.put(auth.authToken(), auth.user());
        return auth;
    }

    public String getUser(String authToken) {
        return sessions.get(authToken);
    }

    public boolean validAuth(String authToken) {
        return (getUser(authToken) != null);
    }

    public boolean validAuth(AuthData auth) {
        return validAuth(auth.authToken);
    }

    public void deleteAuth(String authToken) {
        //TODO
    }

    public void deleteAuth(AuthData auth) {
        deleteAuth(auth.authToken());
    }

}