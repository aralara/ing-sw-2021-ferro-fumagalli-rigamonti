package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.ResourcesMessage;

import java.util.List;

public class ResourcesMarketMessage extends ResourcesMessage {

    public ResourcesMarketMessage(List<Resource> resources) {
        super(resources);
    }
}
