package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.LeaderCardAckMessage;

public class LeaderCardPlayAckMessage extends LeaderCardAckMessage {


    public LeaderCardPlayAckMessage(boolean state){
        super(state);
    }
}
