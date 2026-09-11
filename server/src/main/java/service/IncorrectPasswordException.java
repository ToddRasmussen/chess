package dataaccess;

/**
 * Indicates there was an error as the username was already taken
 */
public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException(String message) {
        super(message);
    }
    public IncorrectPasswordException(String message, Throwable ex) {
        super(message, ex);
    }
}
