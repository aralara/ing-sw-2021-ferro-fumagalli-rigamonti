package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * Server message that notifies the client that he needs to discard their leaders at the start of the game
 */
public class AskLeaderCardDiscardMessage implements ServerActionMessage {

    @Override
    public void doAction(ClientController client) {
        client.askLeaderDiscard();
    }
}
