package it.polimi.ingsw;

import java.util.List;

public class AbilityWarehouse implements SpecialAbility, Storage{

    private ResourceType resourceType;
    private List<Resource> resources;


    public AbilityWarehouse(){

    }


    public boolean checkAdd(List<Resource> resources){
        return false;
    }
}
