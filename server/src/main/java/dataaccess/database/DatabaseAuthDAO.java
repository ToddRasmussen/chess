package dataaccess.database;

import dataaccess.interfaces.AuthDAO;
import dataaccess.memory.MemoryAuthDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {

    private final AuthDAO cache;
    private final String table;

    public DatabaseAuthDAO() {
        cache = new MemoryAuthDAO();
        table = "chess.sessions";
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuth(UserData user) throws Exception {
        AuthData auth = new AuthData(generateToken(), user.username());
        addAuth(auth);
        return auth;
    }

    public void addAuth(AuthData auth) throws Exception {
        String sql = "INSERT INTO " + table + " (authToken, username) VALUES (?, ?);";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1,auth.authToken());
                statement.setString(2,auth.username());
                statement.executeUpdate();
            }
        }
        cache.addAuth(auth);
    }

    public String getUser(String authToken) throws Exception {
        String cache_result = cache.getUser(authToken);
        if (cache_result != null) {
            return cache_result;
        }
        String sql = "SELECT username FROM " + table + " WHERE authToken = ? LIMIT 1;";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, authToken);
                try (var results = statement.executeQuery()) {
                    if (results.next()) {
                        String username = results.getString("username");
                        cache.addAuth(new AuthData(authToken, username));
                        return username;
                    }
                }
            }
        }
        return null;
    }

    public boolean validAuth(String authToken) throws Exception {
        return (getUser(authToken) != null);
    }

    public void deleteAuth(String authToken) throws Exception {
        String sql = "DELETE FROM " + table + " WHERE authToken = ?;";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, authToken);
                statement.executeUpdate();
            }
        }
        cache.deleteAuth(authToken);
    }

    public void reset() throws Exception {
        String sql = "TRUNCATE " + table + ";";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }
        cache.reset();
    }

}
