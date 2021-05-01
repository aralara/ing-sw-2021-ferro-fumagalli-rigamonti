package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class ActivateProductionsAckMessage extends AckMessage {


    public ActivateProductionsAckMessage(boolean state){
        super(state);
    }
}
