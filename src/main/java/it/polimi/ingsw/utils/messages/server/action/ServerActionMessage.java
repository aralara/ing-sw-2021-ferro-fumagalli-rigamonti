package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.Message;

/**
 * Generic server message that represents an action
 */
public interface ServerActionMessage extends Message {
    void doAction(ClientController client);
}
