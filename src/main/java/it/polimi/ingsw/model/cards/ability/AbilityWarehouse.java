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
        //TODO da cambiare (?) per gestire shelf nella playerboard
    }
}
