package dataaccess;

/**
 * Indicates was given a unknown color (was not black or white)
 */
public class UnknownColorException extends Exception{
    public UnknownColorException(String message) {
        super(message);
    }
    public UnknownColorException(String message, Throwable ex) {
        super(message, ex);
    }
}
