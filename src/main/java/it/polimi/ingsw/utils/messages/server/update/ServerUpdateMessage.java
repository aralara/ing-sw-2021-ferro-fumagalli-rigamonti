package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.Message;

/**
 * TODO: fare javadoc
 */
public interface ServerUpdateMessage extends Message {
    void doUpdate(ClientController client);
}
