package it.polimi.ingsw.utils.messages.client;

/**
 * Generic client message that represents a setup action
 */
public abstract class ClientSetupMessage extends ClientMessage {

    /**
     * Does a setup action defined in the specific message
     */
    public abstract Object doSetup();

}
