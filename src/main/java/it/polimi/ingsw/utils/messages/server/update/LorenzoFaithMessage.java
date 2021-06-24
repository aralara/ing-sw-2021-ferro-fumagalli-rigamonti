package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;

/**
 * Server update message for Lorenzo's faith
 */
public class LorenzoFaithMessage implements ServerUpdateMessage {

    private final int faith;


    /**
     * Constructor for a LorenzoFaithMessage given Lorenzo's faith
     * @param faith Lorenzo's faith contained in the LorenzoFaithMessage
     */
    public LorenzoFaithMessage(int faith) {
        this.faith = faith;
    }


    @Override
    public void doUpdate(ClientController client) {
        client.setLorenzoFaith(faith);
    }
}
