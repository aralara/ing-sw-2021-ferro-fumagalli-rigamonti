package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.LeaderCardAckMessage;

public class LeaderCardDiscardAckMessage extends LeaderCardAckMessage {


    public LeaderCardDiscardAckMessage(boolean state){
        super(state);
    }
}
