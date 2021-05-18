package it.polimi.ingsw.utils.messages;

import java.util.UUID;

public class LeaderCardAckMessage extends AckMessage{


    public LeaderCardAckMessage(boolean state){
        super(UUID.randomUUID(), state);    //TODO: temporaneo, per tutti gli ack
    }
}
