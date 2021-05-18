package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.utils.messages.AckMessage;
import it.polimi.ingsw.utils.messages.IdentifiedMessage;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;

import java.util.UUID;

public class ServerActionAckMessage extends AckMessage {

    private ClientActionMessage relativeMessage;

    public ServerActionAckMessage(UUID uuid, boolean state) {
        super(uuid, state);
    }

    public void activateResponse(ClientController client) {
        if (relativeMessage != null) {
            if (isState())
                relativeMessage.doACKResponseAction(client);
            else
                relativeMessage.doNACKResponseAction(client);
        }
    }

    public ClientActionMessage getRelativeMessage() {
        return relativeMessage;
    }

    public void setRelativeMessage(ClientActionMessage relativeMessage) {
        this.relativeMessage = relativeMessage;
    }
}
