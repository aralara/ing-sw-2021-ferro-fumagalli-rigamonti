package it.polimi.ingsw.model.storage;

import java.util.ArrayList;
import java.util.List;

public class Shelf implements Storage{

    private ResourceType resourceType;
    private Resource resources;
    private int level;
    private boolean isLeader;


    public Shelf(){
        resourceType = ResourceType.WILDCARD;
        resources = new Resource();
        level = 0;
        isLeader = false;
    }

    public Shelf(ResourceType resourceType, Resource resources, int level, boolean isLeader) {
        this.resourceType = resourceType;
        this.resources = new Resource(resources.getResourceType(), resources.getQuantity());
        this.level = level;
        this.isLeader = isLeader;
    }

    /**
     * Gets the resourceType attribute
     * @return Returns resourceType value
     */
    public ResourceType getResourceType() {
        return this.resourceType;
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
     * Gets the isLeader attribute
     * @return Returns isLeader value
     */
    public boolean getIsLeader() {
        return this.isLeader;
    }

    /**
     * Checks if a list of  resources can be added to the shelf
     * @param resources Resources to be added
     * @return Returns true if the resources can be added, false otherwise
     */
    public boolean checkAdd(Resource resources){
        if(this.resources.getQuantity() == 0) {
            if(!this.isLeader){
                return resources.getQuantity() <= level;
            }
            else
            {
                return ((resources.getQuantity() <= level)&&(this.getResourceType() == resources.getResourceType()));
            }
        }
        else {
            if(this.resources.getQuantity() + resources.getQuantity() > level) {
                return false;
            }
            else return this.getResourceType() == resources.getResourceType();
        }
    }

    @Override
    public List<Resource> getList() {
        List<Resource> temp = new ArrayList<>();
        temp.add(this.resources);
        return temp;
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        Storage.aggregateResources(resources);
        if(resources.size() > 1) {
            return false;
        }
        else{
            return addResources(resources.get(0));
        }
    }

    /**
     * Adds a resource to the shelf
     * @param resource Resource to be added
     * @return Returns true if the resource is added correctly, false otherwise
     */
    public boolean addResources(Resource resource) {
        if(checkAdd(resource)) {
            if (this.resources.getQuantity() == 0 && !isLeader) {
                this.resourceType = resource.getResourceType();
                this.resources.setResourceType(this.resourceType);
            }
            return this.resources.add(resource);
        }
        return false;
    }

    @Override
    public boolean removeResources(List<Resource> resources){
        Storage.aggregateResources(resources);
        if(resources.size() > 1) {
            return false;
        }
        else{
            return removeResources(resources.get(0));
        }
    }

    /**
     * Removes a resource from the shelf
     * @param resource  Resources to be removed
     * @return Returns true if the resource is removed correctly, false otherwise
     */
    public boolean removeResources(Resource resource){
        return this.resources.sub(resource);
    }
}
