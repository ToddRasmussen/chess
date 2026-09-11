



public class GameDAO {

    private Map<String, GameData> games;

    public GameDAO() {
        this.games = new HashMap<>();
    }

    private String generateID() {
        return UUID.randomUUID().toString();
    }

    public Collection<GameData> listGames() {
        return games;
    }

    public GameData getGame(String gameID) {
        return games.get(gameID);
    }

    public boolean isGame(String gameID) {
        return (getGame(gameID) != null);
    }

    public String getPlayer(String gameID, String playerColor) {
        if (playerColor == "White") {
            return getGame(gameID).whiteUsername();
        } else {
            return getGame(gameID).blackUsername();
        }
        
    }

    public void joinGame(String gameID, String playerColor, String username) {
        GameData game = getGame(gameID);

        if (playerColor == "White") {
            if (game.whiteUsername().isEmpty()) {
                game.whiteUsername = username

            }
            
        }

    }

    public String addGame(GameData game) {
        String gameID = generateID();
        games.put(gameID, game);
        return gameID;
    }

    public String createGame(String gameName) {
        GameData newGame = new GameData( "", "", gameName, new ChessGame());
        String gameID = addGame(newGame);
        return gameID;
    }

}