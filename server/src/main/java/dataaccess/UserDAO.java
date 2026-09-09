


import java.util.HashMap;
import java.util.Map;

public class UserDAO {
    private Map<String, UserData> users;

    public UserDAO() {
        this.users = new HashMap<>();
    }

    public void createUser(UserData user) {
        users.put(user.username,user);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public void clear() {
        this.users = new HashMap<>();
    }
}