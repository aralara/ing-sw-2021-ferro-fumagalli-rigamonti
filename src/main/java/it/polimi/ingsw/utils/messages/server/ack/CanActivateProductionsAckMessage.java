package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class CanActivateProductionsAckMessage extends AckMessage {


    public CanActivateProductionsAckMessage(boolean state){
        super(state);
    }
}
