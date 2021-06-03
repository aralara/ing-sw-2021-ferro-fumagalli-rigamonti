package it.polimi.ingsw.server.model.storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Shelf implements Storage, Serializable {

    private ResourceType resourceType;
    private final Resource resources;
    private final int level;
    private final boolean isLeader;


    /**
     * Shelf default constructor (initializes an empty WILDCARD shelf of level 0)
     */
    public Shelf() {
        resourceType = ResourceType.WILDCARD;
        resources = new Resource();
        level = 0;
        isLeader = false;
    }

    /**
     * Shelf constructor given its level
     * @param level Level of the shelf
     */
    public Shelf(int level) {
        this.resourceType = ResourceType.WILDCARD;
        this.resources = new Resource();
        this.level = level;
        this.isLeader = false;
    }

    /**
     * Shelf constructor with parameters
     * @param resourceType Type of the resources contained in the shelf
     * @param resources Resources contained in the shelf
     * @param level Level of the shelf
     * @param isLeader True if it's a leader shelf
     */
    public Shelf(ResourceType resourceType, Resource resources, int level, boolean isLeader) {
        this.resourceType = resourceType;
        this.resources = new Resource(resources.getResourceType(), resources.getQuantity());
        this.level = level;
        this.isLeader = isLeader;
    }


    /**
     * Checks if a list of shelves doesn't contain a resourceType
     * @param shelves List of shelves to be checked
     * @param resourceType ResourceType to be checked
     * @return Returns true if the list doesn't contain resources of the specified resourceType, false otherwise
     */
    public static boolean isResourceTypeUnique(List<Shelf> shelves, ResourceType resourceType) {
        return shelves.stream().noneMatch(shelf -> !shelf.isLeader() && shelf.getResourceType().equals(resourceType));
    }

    /**
     * Checks if a list of resources can be added to the shelf
     * @param resources Resources to be added
     * @return Returns true if the resources can be added, false otherwise
     */
    public boolean checkAdd(Resource resources) {
        if(this.resources.getQuantity() == 0) {
            if(!this.isLeader)
                return resources.getQuantity() <= level;
            else
                return ((resources.getQuantity() <= level)&&(this.getResourceType() == resources.getResourceType()));
        }
        else {
            if(this.resources.getQuantity() + resources.getQuantity() > level)
                return false;
            else
                return this.getResourceType() == resources.getResourceType();
        }
    }

    @Override
    public boolean addResources(List<Resource> resources) {
        Storage.aggregateResources(resources);
        if(resources.size() > 1)
            //It's not possible to add a list of resources to the shelf without specifying their positions
            return false;
        else
            return addResources(resources.get(0));
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
    public boolean removeResources(List<Resource> resources) {
        Storage.aggregateResources(resources);
        if(resources.size() > 1)
            //It's not possible to remove a list of resources from the shelf without specifying their positions
            return false;
        else
            return removeResources(resources.get(0));
    }

    /**
     * Removes a resource from the shelf
     * @param resource Resources to be removed
     * @return Returns true if the resource is removed correctly, false otherwise
     */
    public boolean removeResources(Resource resource) {
        return this.resources.sub(resource);
    }

    @Override
    public List<Resource> getList() {
        List<Resource> temp = new ArrayList<>();
        temp.add(this.resources);
        return temp;
    }

    /**
     * Method to return a cloned instance for a Shelf
     * @return Returns Shelf cloned object
     */
    public Shelf makeClone() {
        return new Shelf(
                this.getResourceType(),
                new Resource(
                        this.resources.getResourceType(),
                        this.resources.getQuantity()),
                this.getLevel(),
                this.isLeader);
    }

    /**
     * Gets if the shelf is empty
     * @return Returns true if the shelf is empty, false otherwise
     */
    public boolean isEmpty() {
        return getResources().getQuantity() <= 0;
    }

    /**
     * Gets the resources attribute
     * @return Returns resources value
     */
    public Resource getResources() {
        return this.resources;
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
    public void setResourceType(ResourceType resource) {
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
    public boolean isLeader() {
        return this.isLeader;
    }
}
