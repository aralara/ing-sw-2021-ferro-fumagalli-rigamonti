package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class CanBuyDevelopmentCardAckMessage extends AckMessage {


    public CanBuyDevelopmentCardAckMessage(boolean state){
        super(state);
    }
}
