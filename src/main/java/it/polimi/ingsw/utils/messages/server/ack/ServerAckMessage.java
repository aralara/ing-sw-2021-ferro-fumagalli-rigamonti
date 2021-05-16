package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.utils.messages.Message;

public interface ServerAckMessage extends Message {
    void doACK();
}
