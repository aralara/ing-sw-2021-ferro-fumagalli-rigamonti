package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class ConnectionAckMessage extends AckMessage {


    public ConnectionAckMessage(boolean state){
        super(state);
    }
}
