package dataaccess;

import org.junit.jupiter.api.BeforeEach;
import dataaccess.interfaces.AuthDAO;
import dataaccess.database.DatabaseAuthDAO;

public class DatabaseAuthDAOTests {
    @BeforeEach
    public void setUp() {
        try {
            new DatabaseAuthDAO().reset();
        } catch (Exception e) {

        }
    }
}
