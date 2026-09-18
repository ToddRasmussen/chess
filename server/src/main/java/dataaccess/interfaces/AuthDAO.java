package dataaccess.interfaces;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public AuthData createAuth(UserData user);

    public void addAuth(AuthData auth);

    public String getUser(String authToken);

    public boolean validAuth(String authToken);

    public void deleteAuth(String authToken);

    public void reset();
}