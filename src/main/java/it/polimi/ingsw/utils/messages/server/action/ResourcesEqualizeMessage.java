package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.client.ConfirmReadyMessage;

import java.util.List;

/**
 * Server message that notifies the client that he needs to choose some resources at the start of the game
 */
public class ResourcesEqualizeMessage extends ResourcesMessage implements ServerActionMessage {

    public ResourcesEqualizeMessage(List<Resource> resources) {
        super(resources);
    }

    @Override
    public void doAction(ClientController client) {
        client.askResourceEqualize(getResources());
        client.getMessageHandler().sendClientMessage(new ConfirmReadyMessage());
    }
}
