package dataaccess;

import dataaccess.database.DatabaseAuthDAO;
import org.junit.jupiter.api.BeforeEach;

public class DatabaseUserDAOTests {
    @BeforeEach
    public void setUp() {
        try {
            new DatabaseAuthDAO().reset();
        } catch (Exception e) {

        }
    }
}
