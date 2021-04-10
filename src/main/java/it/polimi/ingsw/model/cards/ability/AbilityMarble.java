package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.ResourceType;

public class AbilityMarble implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityMarble(ResourceType resourceType){
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
        board.getAbilityMarbles().add(this.resourceType);
    }

    @Override
    public AbilityMarble makeClone(){
        return new AbilityMarble(this.resourceType);
    }
}
