package it.polimi.ingsw.storage;

import java.util.List;

public class RequestResources implements Storage{

    private List<Resource> requestedResources;
    private StorageType storageType;


    public RequestResources() {

    }


    /**
     * Gets the storageType attribute
     * @return Returns storageType value
     */
    public StorageType getStorageType() {
        return null;
    }

    @Override
    public List<Resource> getList() {
        return null;
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        return false;
    }

    @Override
    public boolean removeResources(List<Resource> resources) {
        return false;
    }

}
