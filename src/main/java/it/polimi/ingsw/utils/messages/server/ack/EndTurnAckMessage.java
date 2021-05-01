package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class EndTurnAckMessage extends AckMessage {


    public EndTurnAckMessage(boolean state){
        super(state);
    }
}
