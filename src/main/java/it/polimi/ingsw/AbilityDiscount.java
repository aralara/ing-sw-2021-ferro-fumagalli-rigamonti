package it.polimi.ingsw;

import java.util.List;

public class AbilityDiscount implements SpecialAbility{

    private ResourceType resourceType;


    public AbilityDiscount(){

    }


    public ResourceType getResourceType(){
        return resourceType;
    }

    public List<Resource> applyDiscount(List<Resource> resources){
        return null;
    }
}
