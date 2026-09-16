package dataaccess.memory;

import java.util.UUID;
import model.AuthData;
import model.UserData;
import dataaccess.interfaces.AuthDAO;
import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    private Map<String, String> sessions;

    public MemoryAuthDAO() {
        this.sessions = new HashMap<>();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(UserData user) {
        AuthData auth = new AuthData(generateToken(), user.username());
        sessions.put(auth.authToken(), auth.username());
        return auth;
    }

    public String getUser(String authToken) {
        return sessions.get(authToken);
    }

    public boolean validAuth(String authToken) {
        return (getUser(authToken) != null);
    }

    public boolean validAuth(AuthData auth) {
        return validAuth(auth.authToken());
    }

    public void deleteAuth(String authToken) {
        sessions.remove(authToken);
    }

    public void deleteAuth(AuthData auth) {
        deleteAuth(auth.authToken());
    }


    public void reset() {
        this.sessions = new HashMap<>();
    }
}