package dataaccess;

/**
 * Indicates color is already taken and cant be overriden
 */
public class ColorAlreadyTakenException extends Exception{
    public ColorAlreadyTakenException(String message) {
        super(message);
    }
    public ColorAlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
