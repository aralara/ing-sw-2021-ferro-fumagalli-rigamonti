package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.*;


public class AbilityWarehouse implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityWarehouse(ResourceType resourceType){
        this.resourceType = resourceType;
    }


    @Override
    public void activateAbility(PlayerBoard board){
        board.addAbilityWarehouse(new Shelf(resourceType, new Resource(resourceType,0), 2, true));
    }
}
