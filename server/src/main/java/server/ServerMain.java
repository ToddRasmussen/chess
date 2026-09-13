package server;

public class ServerMain {
    public static void main(String[] args) {
        var port = 9000; //TODO Revert to 8080 (Conflicts with my OpenWebUI I Host)
        Server server = new Server();
        server.run(port);

        System.out.println("♕ 240 Chess Server running on port " + port);
    }
}