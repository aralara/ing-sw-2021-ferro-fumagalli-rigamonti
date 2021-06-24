package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.IdentifiedMessage;

/**
 * Generic client message
 */
public abstract class ClientMessage extends IdentifiedMessage {

    /**
     * Does an ACK action defined in the specific message
     * @param client ClientController on which the ACK will take effect on
     */
    public abstract void doACKResponseAction(ClientController client);

    /**
     * Does an NACK action defined in the specific message
     * @param client ClientController on which the NACK will take effect on
     */
    public abstract void doNACKResponseAction(ClientController client);
}
