package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.Production;

public class AbilityProduction implements SpecialAbility {

    private Production production;


    public AbilityProduction(Production production){
        this.production = production;
    }


    @Override
    public void activateAbility(PlayerBoard board){
        board.addAbilityProductions(this.production);
    }
}
