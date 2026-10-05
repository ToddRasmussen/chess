package client;

import client.internal.State;

public interface Display {

    public void display(Integer gameID, ServerFacade server, State state);
}
