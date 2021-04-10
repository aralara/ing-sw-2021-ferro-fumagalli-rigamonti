package it.polimi.ingsw.model.cards.ability;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.Production;
import it.polimi.ingsw.model.storage.ResourceType;

public class AbilityProduction implements SpecialAbility {

    private Production production;


    public AbilityProduction(Production production){
        this.production = production;
    }


    /**
     * Gets the resource attribute
     * @return Returns resource value
     */
    public Production getProduction() {
        return production;
    }

    @Override
    public void activateAbility(PlayerBoard board){
        board.getAbilityProductions().add(this.production);
    }

    @Override
    public AbilityProduction makeClone(){
        return new AbilityProduction(this.production.makeClone());
    }
}
