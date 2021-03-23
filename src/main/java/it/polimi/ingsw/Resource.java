package it.polimi.ingsw;

public class Resource implements Requirement {

    ResourceType resourceType;
    int quantity;

    public Resource()
    {

    }

    public ResourceType getResourceType()
    {
        return this.resourceType;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public void setResourceType(ResourceType type)
    {
        this.resourceType = type;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
}
