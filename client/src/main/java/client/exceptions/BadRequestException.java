package client.exceptions;

/**
 * Indicates given a bad request
 */
public class BadRequestException extends Exception{
    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(String message, Throwable ex) {
        super(message, ex);
    }
}
