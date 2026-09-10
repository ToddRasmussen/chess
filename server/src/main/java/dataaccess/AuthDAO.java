



public class AuthDAO {

    private Collection<AuthData> sessions;

    public AuthDAO() {
        this.sessions = new ArrayList<>();
    }

    public void createAuth(AuthData auth) {
        sessions.put(auth);
    }

    public boolean validAuth(AuthData user) {

    }

    public void deleteAuth(AuthData auth) {
        sessions.remove(auth);
    }

    
}