package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;

public abstract class ClientSetupMessage extends ClientMessage {
    public abstract Object doSetup();

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: gestionre variabile ACK
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        //TODO: gestionre variabile ACK
    }
}
