package it.polimi.ingsw;

import java.util.List;

public class AbilityWarehouse implements SpecialAbility, Storage{

    private ResourceType resourceType;
    private List<Resource> resources;


    public AbilityWarehouse(){

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
    public boolean takeResources(List<Resource> resources)
    {
        return false;
    }

    @Override
    public boolean checkAvailability(List<Resource> resources)
    {
        return false;
    }

    public boolean checkAdd(List<Resource> resources){
        return false;
    }
}
