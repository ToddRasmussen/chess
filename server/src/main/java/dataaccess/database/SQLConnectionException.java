package dataaccess.database;

/**
 * Indicates there was an error connecting to the database more of a generic error
 */
public class SQLConnectionException extends Exception{
    public SQLConnectionException(String message) {
        super(message);
    }
    public SQLConnectionException(String message, Throwable ex) {
        super(message, ex);
    }
}
