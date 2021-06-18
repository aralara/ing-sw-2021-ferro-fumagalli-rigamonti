package it.polimi.ingsw.utils.messages.server.ack;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.client.ClientMessage;

import java.util.UUID;

/**
 * TODO: fare javadoc
 */
public class ServerAckMessage extends AckMessage {

    private ClientMessage relativeMessage;


    public ServerAckMessage(UUID uuid, boolean state) {
        super(uuid, state);
    }

    public ServerAckMessage(ClientMessage relativeMessage, boolean state) {
        super(relativeMessage.getUuid(), state);
    }


    public void activateResponse(ClientController client) {
        if (relativeMessage != null) {
            if (isState())
                relativeMessage.doACKResponseAction(client);
            else
                relativeMessage.doNACKResponseAction(client);
        }
    }

    public void setRelativeMessage(ClientMessage relativeMessage) {
        this.relativeMessage = relativeMessage;
    }
}
