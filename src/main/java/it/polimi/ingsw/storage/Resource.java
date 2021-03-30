package it.polimi.ingsw.storage;

import it.polimi.ingsw.cards.requirement.Requirement;

public class Resource implements Requirement {

    private ResourceType resourceType;
    private int quantity;


    public Resource() {

    }


    /**
     * Adds the quantity of the given resource to the quantity of the current resource if the resource types match
     * @param r The resource to add
     */
    public void add(Resource r){

    }

    /**
     * Subtracts the quantity of the given resource to the quantity of the current resource if the resource types match
     * and the result isn't negative
     * @param r The resource to subtract
     */
    public void sub(Resource r){

    }

    /**
     * Gets the resourceType attribute
     * @return Returns resourceType
     */
    public ResourceType getResourceType() {
        return this.resourceType;
    }

    /**
     * Gets the quantity attribute
     * @return Returns quantity
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Sets the resourceType attribute
     * @param type New attribute value
     */
    public void setResourceType(ResourceType type) {
        this.resourceType = type;
    }

    /**
     * Sets the quantity attribute
     * @param quantity New attribute value
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
