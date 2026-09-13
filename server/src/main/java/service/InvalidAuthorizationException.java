package service;

/**
 * Indicates invalid/insufficient authorization
 */
public class InvalidAuthorizationException extends Exception{
    public InvalidAuthorizationException(String message) {
        super(message);
    }
    public InvalidAuthorizationException(String message, Throwable ex) {
        super(message, ex);
    }
}
