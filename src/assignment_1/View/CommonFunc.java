package assignment_1.View;

import assignment_1.Controller.ClientController;

/*
 * Provides the interface of common used functions for GUI.
 */
public interface CommonFunc {
    public void alert(String alertMsg);
    public void registerObserver(ClientController controller);
}
