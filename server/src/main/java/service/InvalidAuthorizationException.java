package dataaccess;

/**
 * Indicates there was an error as the username was already taken
 */
public class InvalidAuthorizationException extends Exception{
    public InvalidAuthorizationException(String message) {
        super(message);
    }
    public InvalidAuthorizationException(String message, Throwable ex) {
        super(message, ex);
    }
}
