package service;

/**
 * Indicates there was an error as the password was incorrect
 */
public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException(String message) {
        super(message);
    }
    public IncorrectPasswordException(String message, Throwable ex) {
        super(message, ex);
    }
}
