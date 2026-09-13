package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomExceptionTests {

    @Test
    public void testAlreadyTakenException() {
        AlreadyTakenException exMsg = new AlreadyTakenException("Already taken");
        Assertions.assertNotNull(exMsg);
        Assertions.assertEquals("Already taken", exMsg.getMessage());
    }

    @Test
    public void testDoesNotExistException() {
        DoesNotExistException exMsg = new DoesNotExistException("Does not exist");
        Assertions.assertNotNull(exMsg);
        Assertions.assertEquals("Does not exist", exMsg.getMessage());
    }

    @Test
    public void testIncorrectPasswordException() {
        IncorrectPasswordException exMsg = new IncorrectPasswordException("Incorrect password");
        Assertions.assertNotNull(exMsg);
        Assertions.assertEquals("Incorrect password", exMsg.getMessage());
    }

    @Test
    public void testInvalidAuthorizationException() {
        InvalidAuthorizationException exMsg = new InvalidAuthorizationException("Invalid authorization");
        Assertions.assertNotNull(exMsg);
        Assertions.assertEquals("Invalid authorization", exMsg.getMessage());
    }
}