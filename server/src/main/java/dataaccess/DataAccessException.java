package dataaccess;

/**
 * Indicates there was an error connecting to the database more of a generic error
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
