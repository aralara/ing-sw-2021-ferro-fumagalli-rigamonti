package it.polimi.ingsw.server.model.storage;

import java.io.Serializable;
import java.util.List;

public class RequestResources implements Storage, Serializable {

    private final List<Resource> requestedResources;
    private final StorageType storageType;


    /**
     * RequestResources constructor with parameters
     * @param requestedResources List of requested resources
     * @param storageType Type of requested storage
     */
    public RequestResources(List<Resource> requestedResources, StorageType storageType) {
        this.requestedResources = requestedResources;
        this.storageType = storageType;
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
                for (Resource resource : resources) {
                    if (this.requestedResources.get(i).getResourceType() == resource.getResourceType()) {
                        this.requestedResources.get(i).sub(resource);
                        if (this.requestedResources.get(i).getQuantity() == 0) {
                            this.requestedResources.remove(i);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<Resource> getList() {
        Storage.aggregateResources(this.requestedResources);
        return this.requestedResources;
    }

    /**
     * Gets the storageType attribute
     * @return Returns storageType value
     */
    public StorageType getStorageType() {
        return this.storageType;
    }
}
