package it.polimi.ingsw.utils.messages;

import it.polimi.ingsw.server.model.storage.Resource;

import java.util.List;

public class ResourcesMessage implements Message {

    private final List<Resource> resources;


    public ResourcesMessage(List<Resource> resources) {
        this.resources = resources;
    }


    public List<Resource> getResources() {
        return resources;
    }
}
