package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class ColorAlreadyTakenException extends Exception{
    public ColorAlreadyTakenException(String message) {
        super(message);
    }
    public ColorAlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
