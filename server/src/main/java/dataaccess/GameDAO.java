



public class GameDAO {

    private Collection<GameData> games;

    public GameDAO() {
        this.games = new ArrayList<>();
    }

    public Collection<GameData> listGames() {
        return games;
    }

    public void updateGame() {
        //TODO
    }

}