package dataaccess.interfaces;

import model.UserData;

public interface UserDAO {

    public void createUser(UserData user) throws Exception ;

    public UserData getUser(String username) throws Exception ;

    public boolean isUser(String username) throws Exception ;

    public boolean validatePassword(String username, String password) throws Exception ;

    public void reset() throws Exception ;
}