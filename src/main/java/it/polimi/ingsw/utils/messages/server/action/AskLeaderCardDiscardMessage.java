package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * TODO: fare javadoc
 */
public class AskLeaderCardDiscardMessage implements ServerActionMessage {

    @Override
    public void doAction(ClientController client) {
        client.askLeaderDiscard();
    }
}
