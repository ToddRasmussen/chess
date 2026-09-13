package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomExceptionTests {

    @Test
    public void testAlreadyTakenException() {
        AlreadyTakenException ex1 = new AlreadyTakenException("Already taken");
        AlreadyTakenException ex2 = new AlreadyTakenException("Already taken", new Throwable());
        Assertions.assertNotNull(ex1);
        Assertions.assertNotNull(ex2);
    }

    @Test
    public void testDoesNotExistException() {
        DoesNotExistException ex1 = new DoesNotExistException("Does not exist");
        DoesNotExistException ex2 = new DoesNotExistException("Does not exist", new Throwable());
        Assertions.assertNotNull(ex1);
        Assertions.assertNotNull(ex2);
    }

    @Test
    public void testIncorrectPasswordException() {
        IncorrectPasswordException ex1 = new IncorrectPasswordException("Incorrect password");
        IncorrectPasswordException ex2 = new IncorrectPasswordException("Incorrect password", new Throwable());
        Assertions.assertNotNull(ex1);
        Assertions.assertNotNull(ex2);
    }

    @Test
    public void testInvalidAuthorizationException() {
        InvalidAuthorizationException ex1 = new InvalidAuthorizationException("Invalid authorization");
        InvalidAuthorizationException ex2 = new InvalidAuthorizationException("Invalid authorization", new Throwable());
        Assertions.assertNotNull(ex1);
        Assertions.assertNotNull(ex2);
    }
}