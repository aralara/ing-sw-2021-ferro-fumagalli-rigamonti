package it.polimi.ingsw.model.storage;

import java.util.ArrayList;
import java.util.List;

public class RequestResources implements Storage{

    private List<Resource> requestedResources;
    private StorageType storageType;


    public RequestResources() {
        requestedResources = new ArrayList<>();
        storageType = null;
    }

    public RequestResources(List<Resource> requestedResources, StorageType storageType) {
        this.requestedResources = requestedResources;
        this.storageType = storageType;
    }


    /**
     * Gets the storageType attribute
     * @return Returns storageType value
     */
    public StorageType getStorageType() {
        return this.storageType;
    }

    @Override
    public List<Resource> getList() {
        return this.requestedResources;
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        this.requestedResources.addAll(resources);
        Storage.aggregateResources(this.requestedResources);
        return true;
    }

    @Override
    public boolean removeResources(List<Resource> resources) {
        Storage.aggregateResources(resources);
        if (Storage.checkContainedResources(this.getList(), resources)) {
            for (int i = 0; i < this.requestedResources.size(); i++) {
                for (int j = 0; j < this.requestedResources.size(); j++) {
                    if (this.requestedResources.get(i).getResourceType() == resources.get(j).getResourceType()) {
                        this.requestedResources.get(i).sub(resources.get(j));
                    }
                }
            }
            return true;
        }
        return false;
    }
}
