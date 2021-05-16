package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;

public class CanBuyDevelopmentCardAckMessage extends AckMessage implements ServerAckMessage {


    public CanBuyDevelopmentCardAckMessage(boolean state) {
        super(state);
    }

    @Override
    public void doACK(ClientController client) {

    }
}
