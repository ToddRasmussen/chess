package client.exceptions;

/**
 * Indicates given a bad request
 */
public class ServerException extends Exception{
    public ServerException(String message) {
        super(message);
    }
    public ServerException(String message, Throwable ex) {
        super(message, ex);
    }
}
