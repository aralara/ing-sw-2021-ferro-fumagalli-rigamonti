package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.Message;

/**
 * Generic server update message
 */
public interface ServerUpdateMessage extends Message {

    /**
     * Does an updated defined in the specific message
     * @param client ClientController on which the update will have effect
     */
    void doUpdate(ClientController client);

}
