package it.polimi.ingsw.model.storage;

import java.util.List;

public class Shelf implements Storage{

    private ResourceType resourceType;
    private List<Resource> resources;
    private int level;


    public Shelf(){

    }


    /**
     * Gets the resourceType attribute
     * @return Returns resourceType value
     */
    public ResourceType getResourceType() {
        return null;
    }

    /**
     * Sets the resourceType attribute
     * @param resource New attribute value
     */
    public void setResourceType(ResourceType resource){
        this.resourceType = resource;
    }

    /**
     * Gets the level attribute
     * @return Returns level value
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Checks if a list of  resources can be added to the shelf
     * @param resources Resources to be added
     * @return Returns true if the resources can be added, false otherwise
     */
    public boolean checkAdd(List<Resource> resources){
        return false;
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
    public boolean removeResources(List<Resource> resources){
        return false;
    }
}
