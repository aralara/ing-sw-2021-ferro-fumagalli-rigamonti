package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.requirement.RequirementRes;
import it.polimi.ingsw.model.storage.*;


public class AbilityWarehouse implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityWarehouse(ResourceType resourceType){
        this.resourceType = resourceType;
    }


    /**
     * Gets the resourceType attribute
     * @return Returns resourceType value
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public void activateAbility(PlayerBoard board){
        board.addAbilityWarehouse(new Shelf(resourceType, new Resource(resourceType,0), 2, true));
    }

    @Override
    public AbilityWarehouse makeClone(){
        return new AbilityWarehouse(this.resourceType);
    }
}
