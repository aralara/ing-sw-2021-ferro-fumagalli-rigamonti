package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;

import java.util.UUID;

public class EndTurnAckMessage extends AckMessage implements ServerAckMessage {

    public EndTurnAckMessage(boolean state) {
        super(UUID.randomUUID(), state);
    }

    @Override
    public void doACK(ClientController client) {

    }
}
