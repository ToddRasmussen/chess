package client.displays;

import client.internal.ServerFacade;
import client.internal.State;

public interface Display {

    void triggerRedraw();

    void display(ServerFacade server, State state);
}
