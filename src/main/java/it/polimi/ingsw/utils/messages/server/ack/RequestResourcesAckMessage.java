package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class RequestResourcesAckMessage extends AckMessage {


    public RequestResourcesAckMessage(boolean state){
        super(state);
    }
}
