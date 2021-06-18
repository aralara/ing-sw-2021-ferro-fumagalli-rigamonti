package it.polimi.ingsw.server.model.storage;

import java.io.Serializable;

/**
 * Handles methods relative to the resource
 */
public class Resource implements Serializable {

    private ResourceType resourceType;
    private int quantity;


    /**
     * Resource default constructor (initializes a WILDCARD resource with 0 quantity)
     */
    public Resource() {
        resourceType = ResourceType.WILDCARD;
        quantity = 0;
    }

    /**
     * Resource constructor with parameters
     * @param resourceType Type of the resource
     * @param quantity Quantity of the resource
     */
    public Resource(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }


    /**
     * Adds the quantity of the given resource to the quantity of the current resource if the resource types match
     * @param r The resource to add
     * @return Returns true if the resource is added correctly, false otherwise
     */
    public boolean add(Resource r) {
        if(this.resourceType == r.getResourceType()) {
            this.quantity += r.getQuantity();
            return true;
        }
        return false;
    }

    /**
     * Subtracts the quantity of the given resource to the quantity of the current resource if the resource types match
     * and the result isn't negative
     * @param r The resource to subtract
     * @return Returns true if the resource is subtracted correctly, false otherwise
     */
    public boolean sub(Resource r) {
        if(this.resourceType == r.getResourceType() && this.quantity >= r.getQuantity()) {
            this.quantity -= r.getQuantity();
            if(this.quantity == 0)
                this.resourceType = ResourceType.WILDCARD;
            return true;
        }
        return false;
    }

    /**
     * Returns a cloned instance of the Resource
     * @return Returns the cloned object
     */
    public Resource makeClone() {
        return new Resource(this.resourceType,this.quantity);
    }

    /**
     * Gets the resourceType attribute
     * @return Returns resourceType
     */
    public ResourceType getResourceType() {

        return this.resourceType;
    }

    /**
     * Sets the resourceType attribute
     * @param type New attribute value
     */
    public void setResourceType(ResourceType type) {
        this.resourceType = type;
    }

    /**
     * Gets the quantity attribute
     * @return Returns quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the quantity attribute
     * @param quantity New attribute value
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
