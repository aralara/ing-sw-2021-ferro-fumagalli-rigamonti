package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.utils.listeners.Listeners;

/**
 * Handles methods relative to the marble ability
 */
public class AbilityMarble implements SpecialAbility {

    private final ResourceType resourceType;


    /**
     * Constructor for an AbilityMarble
     * @param resourceType Resource type of the wildcard marble
     */
    public AbilityMarble(ResourceType resourceType) {
        this.resourceType = resourceType;
    }


    @Override
    public void activateAbility(PlayerBoard board) {
        board.getAbilityMarbles().add(this.resourceType);
        board.fireUpdate(Listeners.BOARD_ABILITY_MARB.value(), board.getAbilityMarbles());
    }

    @Override
    public String abilityToString() {
        return  " • Special ability: You can can get a " + resourceType + " from the white marbles in the market \n";
    }

    /**
     * Gets the resourceType attribute
     * @return Returns resourceType value
     */
    public ResourceType getResourceType() {
        return resourceType;
    }
}
