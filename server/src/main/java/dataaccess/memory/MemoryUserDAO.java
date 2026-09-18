package dataaccess.memory;

import java.util.HashMap;
import java.util.Map;
import model.UserData;
import dataaccess.interfaces.UserDAO;

public class MemoryUserDAO implements UserDAO {
    private Map<String, UserData> users;

    public MemoryUserDAO() {
        this.users = new HashMap<>();
    }

    public void createUser(UserData user) {
        users.put(user.username(),user);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public boolean isUser(String username) {
        return (getUser(username) != null);
    }

    public boolean validatePassword(String username, String password) {
    UserData user = getUser(username);
    if (user == null || user.password() == null || password == null) {
        return false;
    }
    return user.password().equals(password);
    }

    public void reset() {
        this.users = new HashMap<>();
    }
}