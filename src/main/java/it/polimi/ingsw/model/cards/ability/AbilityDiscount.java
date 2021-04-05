package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.*;

public class AbilityDiscount implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityDiscount(ResourceType resourceType){
        this.resourceType = resourceType;
    }


    @Override
    public void activateAbility(PlayerBoard board){
        board.addAbilityDiscounts(this.resourceType);

    }
}
