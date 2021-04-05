package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.ResourceType;

public class AbilityMarble implements SpecialAbility {

    private ResourceType resourceType;


    public AbilityMarble(ResourceType resourceType){
        this.resourceType = resourceType;
    }


    @Override
    public void activateAbility(PlayerBoard board){
        board.addAbilityMarbles(this.resourceType);
    }
}
