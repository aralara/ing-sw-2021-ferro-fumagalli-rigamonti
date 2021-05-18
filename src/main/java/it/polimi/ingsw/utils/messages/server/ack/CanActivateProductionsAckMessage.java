package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;

import java.util.UUID;

public class CanActivateProductionsAckMessage extends AckMessage implements ServerAckMessage {

    public CanActivateProductionsAckMessage(boolean state){
        super(UUID.randomUUID(), state);    //TODO: temporaneo, per tutti gli ack
    }

    @Override
    public void doACK(ClientController client) {

    }
}
