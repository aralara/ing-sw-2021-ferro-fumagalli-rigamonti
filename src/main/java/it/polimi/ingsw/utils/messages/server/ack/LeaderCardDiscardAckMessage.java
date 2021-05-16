package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.LeaderCardAckMessage;
import it.polimi.ingsw.utils.messages.server.action.ServerActionMessage;

public class LeaderCardDiscardAckMessage extends LeaderCardAckMessage implements ServerAckMessage {

    public LeaderCardDiscardAckMessage(boolean state) {
        super(state);
    }

    @Override
    public void doACK(ClientController client) {

    }
}
