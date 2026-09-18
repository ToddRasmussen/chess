package dataaccess.database;

import dataaccess.interfaces.AuthDAO;
import dataaccess.memory.MemoryAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {

    private final AuthDAO cache;

    public DatabaseAuthDAO() {
        cache = new MemoryAuthDAO();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(UserData user) {
        AuthData auth = new AuthData(generateToken(), user.username());
        addAuth(auth);
        return auth;
    }

    public void addAuth(AuthData auth) {
        String sql = "INSERT INTO chess.sessions (authToken, username) VALUES (?, ?)";
        //TODO
        cache.addAuth(auth);
    }

    public String getUser(String authToken) {
        String cache_result = cache.getUser(authToken);
        if (cache_result != null) {
            return cache_result;
        }
        String sql = "SELECT(1) username FROM chess.sessions WHERE authToken == ?";
        //TODO
        cache.addAuth(new AuthData(authToken, username));
        return username;
    }

    public boolean validAuth(String authToken) {
        if (cache.validAuth(authToken)) {
            return true;
        }
        String sql = "SELECT(1) FROM chess.sessions WHERE authToken == ?";
        //TODO
        cache.addAuth(new AuthData(authToken, username));
        return exists;
    }

    public void deleteAuth(String authToken) {
        cache.deleteAuth(authToken);
        String sql = "TODO";
        //TODO
    }

    public void reset() {
        cache.reset();
        String sql = "TRUNCATE chess.sessions";
        //TODO
    }

}
