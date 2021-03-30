package it.polimi.ingsw.cards.ability;

import it.polimi.ingsw.storage.ResourceType;

public class AbilityMarble implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityMarble(){

    }


    /**
     * Gets the resourceType attribute
     * @return Returns resourceType
     */
    public ResourceType getResourceType(){
        return resourceType;
    }
}
