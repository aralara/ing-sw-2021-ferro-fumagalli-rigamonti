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
            for (int i=0;i<this.resources.size();i++) {
                for(int j=0;j<this.resources.size();j++) {
                    if(this.resources.get(i).getResourceType() == resources.get(j).getResourceType()) {
                        this.resources.get(i).sub(resources.get(j));
                    }
                }
            }
            return true;
        }
        return false;
    }
}
