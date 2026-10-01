package client;

import client.exceptions.AlreadyTakenException;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static int port = 8080;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(ServerFacadeTests.port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void reset() {
        try {
            new ServerFacade(port).reset();
        } catch (Exception _) {

        }
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
    }

    @Test
    public void registerDuplicateTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        Assertions.assertThrows(AlreadyTakenException.class,() -> facade.register(new UserData("user", "password", "email@example.com")));
    }

    @Test
    public void loginTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        Assertions.assertDoesNotThrow(() -> facade.login(new UserData("user", "password", "email@example.com")));
    }


    @Test
    public void logoutTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void createGameTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        Assertions.assertDoesNotThrow(() -> facade.newGame("game"));
    }

    @Test
    public void createGameDuplicateNameTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        Assertions.assertDoesNotThrow(() -> facade.newGame("game"));
        Assertions.assertDoesNotThrow(() -> facade.newGame("game"));
    }

    @Test
    public void gameTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        int gameID = Assertions.assertDoesNotThrow(() -> facade.newGame("game"));
        GameData gameData = Assertions.assertDoesNotThrow(() -> facade.game(gameID));
        Assertions.assertEquals("game", gameData.getGameName());
    }

    @Test
    public void joinGameTest() {
        ServerFacade facade = new ServerFacade(port);
        Assertions.assertDoesNotThrow(() -> facade.register(new UserData("user", "password", "email@example.com")));
        int gameID = Assertions.assertDoesNotThrow(() -> facade.newGame("game"));
        Assertions.assertDoesNotThrow(() -> facade.joinGame(gameID, "WHITE"));
    }

}
