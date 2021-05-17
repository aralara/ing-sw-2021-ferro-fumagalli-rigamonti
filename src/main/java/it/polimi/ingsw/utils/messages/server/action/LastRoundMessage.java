package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;

public class LastRoundMessage implements ServerActionMessage {

    @Override
    public void doAction(ClientController client) {
        client.notifyLastRound();
    }

}
