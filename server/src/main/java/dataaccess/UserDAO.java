package dataaccess;

import java.util.HashMap;
import java.util.Map;
import model.UserData;

public class UserDAO {
    private Map<String, UserData> users;

    public UserDAO() {
        this.users = new HashMap<>();
    }

    public void createUser(UserData user) {
        users.put(user.username(),user);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void clear() {
        this.users = new HashMap<>();
    }

    public boolean isUser(String username) {
        return (getUser(username) != null);
    }

    public boolean isUser(UserData user) {
        return isUser(user.username());
    }

    public boolean validatePassword(String username, String password) {
    UserData user = getUser(username);
    if (user == null || user.password() == null || password == null) {
        return false;
    }
    return user.password().equals(password);
    }

    public boolean validatePassword(UserData user) {
        return validatePassword(user.username(), user.password());
    }


    public void reset() {
        this.users = new HashMap<>();
    }
}