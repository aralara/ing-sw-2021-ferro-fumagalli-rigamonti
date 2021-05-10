package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.ResourcesMessage;

import java.util.List;

public class ResourcesEqualizeMessage extends ResourcesMessage implements ServerActionMessage {

    public ResourcesEqualizeMessage(List<Resource> resources) {
        super(resources);
    }

    @Override
    public void doAction(CLI client) {
        client.askResourcesToEqualize(this);
    }
}
