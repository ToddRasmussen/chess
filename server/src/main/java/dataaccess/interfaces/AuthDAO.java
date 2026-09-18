package dataaccess.interfaces;

import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public AuthData createAuth(UserData user) throws Exception;

    public void addAuth(AuthData auth) throws Exception;

    public String getUser(String authToken) throws Exception;

    public boolean validAuth(String authToken) throws Exception;

    public void deleteAuth(String authToken) throws Exception;

    public void reset() throws Exception;
}