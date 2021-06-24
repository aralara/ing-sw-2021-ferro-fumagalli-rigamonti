package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.client.ClientMessage;

import java.util.UUID;

/**
 * Server message that serves as an ACK or NACK to a client message
 */
public class ServerAckMessage extends AckMessage {

    private ClientMessage relativeMessage;


    /**
     * Constructor for a ServerAckMessage given a UUID code and its state
     * @param uuid Unique code that binds an ACK message to its original message
     * @param state Identifies the message as an ACK or a NACK
     */
    public ServerAckMessage(UUID uuid, boolean state) {
        super(uuid, state);
    }

    /**
     * Constructor for a ServerAckMessage given a relative message and its state
     * @param relativeMessage Message to which the ACK is tied to
     * @param state Identifies the message as an ACK or a NACK
     */
    public ServerAckMessage(ClientMessage relativeMessage, boolean state) {
        super(relativeMessage.getUuid(), state);
    }


    /**
     * Activates the response method inside the original message
     * @param client Client that will be subject to the response
     */
    public void activateResponse(ClientController client) {
        if (relativeMessage != null) {
            if (isState())
                relativeMessage.doACKResponseAction(client);
            else
                relativeMessage.doNACKResponseAction(client);
        }
    }

    /**
     * Sets the relativeMessage attribute
     * @param relativeMessage New value for the attribute
     */
    public void setRelativeMessage(ClientMessage relativeMessage) {
        this.relativeMessage = relativeMessage;
    }
}
