package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.*;


public class AbilityWarehouse implements SpecialAbility {

    private final ResourceType resourceType;


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
        board.getWarehouse().addShelf(new Shelf(resourceType, new Resource(resourceType,0), 2, true));
    }

    @Override
    public String abilityToString(){
        return  " • Special ability: You can can gain an extra shelf to contain 2 units of " + resourceType + "\n";
    }
}
