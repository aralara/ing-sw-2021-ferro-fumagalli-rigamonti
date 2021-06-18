package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

/**
 * TODO: fare javadoc
 */
public abstract class ResourcesMessage implements Message {

    private final List<Resource> resources;


    public ResourcesMessage(List<Resource> resources) {
        this.resources = resources;
    }


    public List<Resource> getResources() {
        return resources;
    }
}
