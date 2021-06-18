package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.view.VirtualView;

/**
 * Generic client message that represents an action
 */
public abstract class ClientActionMessage extends ClientMessage {
    public abstract void doAction(VirtualView view);
}
