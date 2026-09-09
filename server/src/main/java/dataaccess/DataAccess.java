package server;


import java.util.Map;
import java.util.Collection;


public boolean isExistingUsername(String username) {
    //TODO
}

public boolean isExistingEmail(String email) {
    //TODO
}

public void registerNewUser(String username, String email, String password) {
    //TODO
}

public static String generateAuthToken() {
    String authToken = UUID.randomUUID().toString();
    //TODO: To Database
    return authToken;
}

public boolean validatePassword(String username, String password) {
    //TODO:
}




private String checkAuthentication(String authToken) {
    //TODO: Logic to check if AuthToken is valid (if not return null)

    //TODO: Logic to get username of authToken
}