package dataaccess.database;

import dataaccess.interfaces.UserDAO;
import dataaccess.memory.MemoryUserDAO;
import model.UserData;

public class DatabaseUserDAO implements UserDAO {

    private final UserDAO cache;

    public DatabaseUserDAO() {
        cache = new MemoryUserDAO();
    }

    public void createUser(UserData user) {
        String sql = "INSERT INTO chess.users (username, hashed_password, email) VALUES (?,?,?)";
        //TODO
        cache.createUser(user);
    }

    public UserData getUser(String username) {
        UserData cached_value = cache.getUser(username);
        if (cached_value != null) {
            return cached_value;
        }
        String sql = "SELECT FROM chess.users WHERE username == ?";
        //TODO
        cache.createUser(user);
        return user;
    }

    public boolean isUser(String username) {
        return getUser(username) != null;
    }

    public boolean validatePassword(String username, String password) {
        //TODO
        return false;//REMOVE
    }

    public void reset() {
        String sql = "TRUNCATE chess.users";
        cache.reset();
    }
}
