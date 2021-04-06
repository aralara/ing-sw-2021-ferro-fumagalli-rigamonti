package it.polimi.ingsw.model.storage;

public class Resource {

    private ResourceType resourceType;
    private int quantity;


    public Resource() {
        resourceType = ResourceType.WILDCARD;
        quantity = 0;
    }

    public Resource(ResourceType resourceType, int quantity) {
        this.resourceType = resourceType;
        this.quantity = quantity;
    }


    /**
     * Method to return a cloned instance for a Resource
     * @return Returns Resource cloned object
     */
    public Resource makeClone() {
        return new Resource(this.resourceType,this.quantity);
    }

    /**
     * Adds the quantity of the given resource to the quantity of the current resource if the resource types match
     * @param r The resource to add
     * @return Returns true if the resource is added correctly, false otherwise
     */
    public boolean add(Resource r){
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
    public boolean sub(Resource r){
        if((this.resourceType == r.getResourceType())&&(this.quantity >= r.getQuantity())) {
            this.quantity -= r.getQuantity();
            return true;
        }
        return false;
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
