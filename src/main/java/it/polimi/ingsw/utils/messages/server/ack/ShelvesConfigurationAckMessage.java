package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.AckMessage;

public class ShelvesConfigurationAckMessage extends AckMessage {


    public ShelvesConfigurationAckMessage(boolean state){
        super(state);
    }
}
