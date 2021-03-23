package it.polimi.ingsw;

import java.util.List;

public class AbilityWarehouse implements SpecialAbility, Storage{

    private ResourceType resourceType;
    private List<Resource> resources;


    public AbilityWarehouse(){

    }

    public List<Resource> getList()
    {
        return null;
    }

    public boolean addResources(List<Resource> resources)
    {
        return false;
    }

    public boolean takeResources(List<Resource> resources)
    {
        return false;
    }

    public boolean checkAvailability(List<Resource> resources)
    {
        return false;
    }

    public boolean checkAdd(List<Resource> resources){
        return false;
    }
}
