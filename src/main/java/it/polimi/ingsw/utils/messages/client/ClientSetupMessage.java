package it.polimi.ingsw.utils.messages.client;

/**
 * Generic client message that represents a setup action
 */
public abstract class ClientSetupMessage extends ClientMessage {
    public abstract Object doSetup();
}
