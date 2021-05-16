package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;

public class SelectMarketAckMessage extends AckMessage implements ServerAckMessage  {

    public SelectMarketAckMessage(boolean state) {
        super(state);
    }

    @Override
    public void doACK(ClientController client) {

    }
}
