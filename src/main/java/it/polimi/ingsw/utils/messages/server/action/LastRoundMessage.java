package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

/**
 * Server message that notifies the player of the last turn
 */
public class LastRoundMessage implements ServerActionMessage {

    @Override
    public void doAction(ClientController client) {
        client.notifyLastRound();
    }

}
