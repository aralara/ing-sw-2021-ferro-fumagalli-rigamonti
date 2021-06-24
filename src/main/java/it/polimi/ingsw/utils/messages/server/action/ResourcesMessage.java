package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

/**
 * Generic server message that contains a list of resources
 */
public abstract class ResourcesMessage implements Message {

    private final List<Resource> resources;


    /**
     * Generic constructor for a ResourcesMessage given a list of resources
     * @param resources Resources contained in the ResourcesMessage
     */
    public ResourcesMessage(List<Resource> resources) {
        this.resources = resources;
    }


    /**
     * Gets the resources attribute
     * @return Returns the value of resources
     */
    public List<Resource> getResources() {
        return resources;
    }
}
