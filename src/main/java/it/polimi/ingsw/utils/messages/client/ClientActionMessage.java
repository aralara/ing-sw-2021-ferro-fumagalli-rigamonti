package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.view.VirtualView;

public abstract class ClientActionMessage extends ClientMessage {

    public abstract void doAction(VirtualView view);

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: gestionre variabile ACK
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        //TODO: gestionre variabile ACK
    }
}
