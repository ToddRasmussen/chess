package dataaccess.database;

import dataaccess.interfaces.UserDAO;
import dataaccess.memory.MemoryUserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseUserDAO implements UserDAO {

    private final UserDAO cache;
    private final String table;

    public DatabaseUserDAO() {
        cache = new MemoryUserDAO();
        table = "chess.users";
    }

    private String getHash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public void createUser(UserData user) throws Exception {
        String hashedPassword = getHash(user.password());
        String sql = "INSERT INTO " + table + " (username, hashed_password, email) VALUES (?,?,?);";
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement(sql)) {
                statement.setString(1, user.username());
                statement.setString(2, hashedPassword);
                statement.setString(3, user.email());
                statement.executeUpdate();
            }
        }
        UserData cachedUser = new UserData(user.username(), hashedPassword, user.email());
        cache.createUser(cachedUser);
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

    public boolean validatePassword(String username, String password) throws Exception {
        UserData user = getUser(username);
        if (user == null || user.password() == null || password == null) {
            return false;
        }
        return BCrypt.checkpw(password, user.password());
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

    public void resetCache() throws Exception {
        cache.reset();
    }
}
