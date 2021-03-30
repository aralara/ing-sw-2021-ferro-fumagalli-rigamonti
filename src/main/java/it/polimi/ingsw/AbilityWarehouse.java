package it.polimi.ingsw;

import java.util.List;

public class AbilityWarehouse implements SpecialAbility, Storage{

    private Shelf shelf;


    public AbilityWarehouse(){

    }


    /**
     * Gets the shelf resourceType
     * @return Returns resourceType
     */
    public ResourceType getResourceType() {
        return null;
    }

    @Override
    public List<Resource> getList()
    {
        return null;
    }

    @Override
    public boolean addResources(List<Resource> resources)
    {
        return false;
    }

    @Override
    public boolean removeResources(List<Resource> resources)
    {
        return false;
    }
}
