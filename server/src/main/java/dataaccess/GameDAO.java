



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

    public void joinGame(String gameID, String playerColor, String username) throws ColorAlreadyTakenException {
        GameData game = getGame(gameID);
        if (playerColor == "White") {
            if (game.whiteUsername().isEmpty()) {
                game.whiteUsername = username;
            } else {
                throw new ColorAlreadyTakenException("White is already taken");
            }
        } else if (playerColor == "Black") {
            if (game.blackUsername.isEmpty()) {
                game.blackUsername = username;
            } else {
                throw new ColorAlreadyTakenException("Black is already taken");
            }
        } else {
            throw new UnknownColorException("Unknown Color");
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