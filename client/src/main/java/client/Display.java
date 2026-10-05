package client;

import client.internal.State;

public interface Display {

    void triggerRedraw();

    void display(Integer gameID, ServerFacade server, State state);
}
