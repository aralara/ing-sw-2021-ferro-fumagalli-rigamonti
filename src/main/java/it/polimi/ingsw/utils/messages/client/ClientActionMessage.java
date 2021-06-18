package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.view.VirtualView;

/**
 * TODO: fare javadoc
 */
public abstract class ClientActionMessage extends ClientMessage {
    public abstract void doAction(VirtualView view);
}
