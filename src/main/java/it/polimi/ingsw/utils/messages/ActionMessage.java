package it.polimi.ingsw.utils.messages;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;

public interface ActionMessage extends Message{
    void doAction(VirtualView view, Controller controller);
}
