package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class SelectMarketAckMessage extends AckMessage {


    public SelectMarketAckMessage(boolean state){
        super(state);
    }
}
