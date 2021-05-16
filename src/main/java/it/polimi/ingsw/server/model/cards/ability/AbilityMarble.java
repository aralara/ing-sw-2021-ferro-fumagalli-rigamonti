package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.utils.listeners.Listeners;

public class AbilityMarble implements SpecialAbility {

    private final ResourceType resourceType;


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
        board.fireUpdate(Listeners.BOARD_ABILITY_MARB.value(), board.getAbilityMarbles());
    }
}
