package it.polimi.ingsw.server.model.storage;

import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.server.PlayerListened;

import java.util.ArrayList;
import java.util.List;

public class Strongbox extends PlayerListened implements Storage {

    private final List<Resource> resources;


    /**
     * Strongbox default constructor
     */
    public Strongbox() {
        resources = new ArrayList<>();
    }


    @Override
    public boolean addResources(List<Resource> resources) {
        this.resources.addAll(resources);
        Storage.aggregateResources(this.resources);
        fireUpdate(Listeners.BOARD_STRONGBOX.value(), this.resources);
        return true;
    }

    @Override
    public boolean removeResources(List<Resource> resources) {
        Storage.aggregateResources(resources);
        if(Storage.checkContainedResources(this.getList(), resources)) {
            for (Resource resource : this.resources) {
                for (Resource value : resources) {
                    if (resource.getResourceType() == value.getResourceType())
                        resource.sub(value);
                }
            }
            this.resources.removeIf(resource -> resource.getResourceType() == ResourceType.WILDCARD);
            fireUpdate(Listeners.BOARD_STRONGBOX.value(), this.resources);
            return true;
        }
        return false;
    }

    @Override
    public List<Resource> getList() {
        return this.resources;
    }
}
