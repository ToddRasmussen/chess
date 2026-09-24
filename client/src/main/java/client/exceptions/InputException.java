package client.exceptions;

/**
 * Indicates given a bad request
 */
public class InputException extends Exception{
    public InputException(String message) {
        super(message);
    }
    public InputException(String message, Throwable ex) {
        super(message, ex);
    }
}
