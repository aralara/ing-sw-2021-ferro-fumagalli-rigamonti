package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.*;


public class AbilityDiscount implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityDiscount(ResourceType resourceType){
        this.resourceType = resourceType;
    }


    /**
     * Gets the resource attribute
     * @return Returns resource value
     */
    public ResourceType getResourceType() {
        return resourceType;
    }

    @Override
    public void activateAbility(PlayerBoard board){
        board.getAbilityDiscounts().add(this.resourceType);

    }
}
