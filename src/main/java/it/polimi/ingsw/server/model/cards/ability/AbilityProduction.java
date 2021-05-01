package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.Production;

public class AbilityProduction implements SpecialAbility {

    private final Production production;


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
}
