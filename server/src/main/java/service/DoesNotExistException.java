package dataaccess;

/**
 * Indicates there was an error as the username was already taken
 */
public class DoesNotExistException extends Exception{
    public DoesNotExistException(String message) {
        super(message);
    }
    public DoesNotExistException(String message, Throwable ex) {
        super(message, ex);
    }
}
