package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.IdentifiedMessage;

/**
 * TODO: fare javadoc
 */
public abstract class ClientMessage extends IdentifiedMessage {

    public abstract void doACKResponseAction(ClientController client);

    public abstract void doNACKResponseAction(ClientController client);
}
