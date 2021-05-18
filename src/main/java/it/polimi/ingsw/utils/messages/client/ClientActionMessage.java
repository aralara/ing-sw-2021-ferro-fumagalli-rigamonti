package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.IdentifiedMessage;

public abstract class ClientActionMessage extends IdentifiedMessage {

    public abstract void doAction(VirtualView view, Controller controller);

    public abstract void doACKResponseAction(ClientController client);

    public abstract void doNACKResponseAction(ClientController client);
}
