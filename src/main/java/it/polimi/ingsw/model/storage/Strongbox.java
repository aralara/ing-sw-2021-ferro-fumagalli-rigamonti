package it.polimi.ingsw.model.storage;

import java.util.ArrayList;
import java.util.List;

public class Strongbox implements Storage{

    List<Resource> resources;


    public Strongbox() {
        resources = new ArrayList<>();
    }


    @Override
    public List<Resource> getList() {
        return this.resources;
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        this.resources.addAll(resources);
        Storage.aggregateResources(this.resources);
        return true;
    }

    @Override
    public boolean removeResources(List<Resource> resources) {
        Storage.aggregateResources(resources);
        if(Storage.checkContainedResources(this.getList(), resources)) {
            for (Resource resource : this.resources) {
                for (Resource value : resources) {
                    if (resource.getResourceType() == value.getResourceType()) {
                        resource.sub(value);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
