package dataaccess.interfaces;

import model.UserData;

public interface UserDAO {

    public void createUser(UserData user);

    public UserData getUser(String username);

    public boolean isUser(String username);

    public boolean validatePassword(String username, String password);

    public void reset();
}