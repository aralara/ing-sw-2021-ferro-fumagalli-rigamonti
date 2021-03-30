package it.polimi.ingsw.cards.ability;

import it.polimi.ingsw.storage.*;

import java.util.List;

public class AbilityDiscount implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityDiscount(){

    }


    /**
     * Gets the resourceType attribute
     * @return Returns resourceType
     */
    public ResourceType getResourceType(){
        return resourceType;
    }

    /**
     * Removes the discounted resource from a given list of resources
     * @param resources List of resources to be discounted
     * @return Returns a list of discounted resources
     */
    public List<Resource> applyDiscount(List<Resource> resources){
        return null;
    }
}
