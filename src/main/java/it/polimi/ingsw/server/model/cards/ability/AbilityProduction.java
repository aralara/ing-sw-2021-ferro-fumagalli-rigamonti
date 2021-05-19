package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.utils.listeners.Listeners;

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
        board.fireUpdate(Listeners.BOARD_ABILITY_PROD.value(), board.getAbilityProductions());
    }

    @Override
    public String abilityToString(){

        return " • Special ability: You can can gain access to the following production: \n "
                + production.productionToPrint();
    }
}
