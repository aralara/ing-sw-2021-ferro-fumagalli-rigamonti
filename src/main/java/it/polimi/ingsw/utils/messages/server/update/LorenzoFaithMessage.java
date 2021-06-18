package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;

/**
 * Server update message for Lorenzo's faith
 */
public class LorenzoFaithMessage implements ServerUpdateMessage {

    private final int faith;


    public LorenzoFaithMessage(int faith) {
        this.faith = faith;
    }


    public int getFaith() {
        return faith;
    }

    @Override
    public void doUpdate(ClientController client) {
        client.setLorenzoFaith(faith);
    }
}
