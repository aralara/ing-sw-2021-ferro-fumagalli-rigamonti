package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.Message;

public interface ClientActionMessage extends Message {

    void doAction(VirtualView view, Controller controller);

    void doACKResponseAction(ClientController client);

    void doNACKResponseAction(ClientController client);
}
