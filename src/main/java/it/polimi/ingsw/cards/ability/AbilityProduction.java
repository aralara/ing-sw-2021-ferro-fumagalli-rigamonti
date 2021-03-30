package it.polimi.ingsw.cards.ability;

import it.polimi.ingsw.storage.Production;

public class AbilityProduction implements SpecialAbility {

    private Production production;


    public AbilityProduction(){

    }


    /**
     * Gets the production attribute
     * @return Returns production
     */
    public Production getProduction(){
        return production;
    }
}
