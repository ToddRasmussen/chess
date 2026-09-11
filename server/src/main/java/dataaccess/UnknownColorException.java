package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class UnknownColorException extends Exception{
    public UnknownColorException(String message) {
        super(message);
    }
    public UnknownColorException(String message, Throwable ex) {
        super(message, ex);
    }
}
