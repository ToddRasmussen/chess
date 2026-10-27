package client.internal;

public enum State {
    PRELOGIN,
    POSTLOGIN,
    BLACKTEAM,
    WHITETEAM,
    OBSERVER,
    OFF;
    public static String prefix(State state) {
        return "[" + state.toString() + "] >>> ";
    }
    public String prefix() {
        return State.prefix(this);
    }
}