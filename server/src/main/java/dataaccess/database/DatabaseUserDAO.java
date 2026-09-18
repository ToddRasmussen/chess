package dataaccess.database;

import dataaccess.interfaces.UserDAO;
import dataaccess.memory.MemoryUserDAO;
import model.UserData;

public class DatabaseUserDAO implements UserDAO {

    private final UserDAO cache;
    private final String table;

    public DatabaseUserDAO() {
        cache = new MemoryUserDAO();
        table = "chess.users";
    }

    private String getHash(String password) {
        return password;
    }

    public void createUser(UserData user)  throws Exception {
        String sql = "INSERT INTO " + table + " (username, hashed_password, email) VALUES (?,?,?);";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, user.username());
                statement.setString(2, getHash(user.password()));
                statement.setString(3, user.email());
                statement.executeUpdate();
            }
        }
        cache.createUser(user);
    }

    public UserData getUser(String username)  throws Exception {
        UserData cached_value = cache.getUser(username);
        if (cached_value != null) {
            return cached_value;
        }
        String sql = "SELECT hashed_password, email FROM " + table + " WHERE username = ? LIMIT 1;";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                try (var results = statement.executeQuery()) {
                    if (results.next()) {
                        String password = results.getString("hashed_password");
                        String email = results.getString("email");
                        UserData user = new UserData(username,password,email);
                        cache.createUser(user);
                        return user;
                    }
                }
            }
        }
        return null;
    }

    public boolean isUser(String username)  throws Exception {
        return getUser(username) != null;
    }

    public boolean validatePassword(String username, String password)  throws Exception {
        UserData user = getUser(username);
        if (user == null || user.password() == null || password == null) {
            return false;
        }
        String hashed_password = getHash(password);
        return user.password().equals(hashed_password);
    }

    public void reset()  throws Exception {
        String sql = "TRUNCATE " + table + ";";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.executeUpdate();
            }
        }
        cache.reset();
    }
}
