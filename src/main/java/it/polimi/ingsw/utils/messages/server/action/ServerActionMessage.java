package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.Message;

/**
 * Generic server message that represents an action
 */
public interface ServerActionMessage extends Message {

    /**
     * Does an action defined in the specific message
     * @param client ClientController on which the action will have effect
     */
    void doAction(ClientController client);

}
