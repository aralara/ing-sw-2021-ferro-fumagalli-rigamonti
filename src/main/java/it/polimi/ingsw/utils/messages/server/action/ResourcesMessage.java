package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

/**
 * Generic server message that contains a list of resources
 */
public abstract class ResourcesMessage implements Message { //TODO: ha senso estenderlo al di fuori dei messaggi di azione e del server?

    private final List<Resource> resources;


    public ResourcesMessage(List<Resource> resources) {
        this.resources = resources;
    }


    public List<Resource> getResources() {
        return resources;
    }
}
