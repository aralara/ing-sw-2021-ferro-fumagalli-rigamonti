package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.view.VirtualView;

/**
 * Generic client message that represents an action
 */
public abstract class ClientActionMessage extends ClientMessage {

    /**
     * Does an action defined in the specific message
     * @param view View on which the action will have effect
     */
    public abstract void doAction(VirtualView view);

}
