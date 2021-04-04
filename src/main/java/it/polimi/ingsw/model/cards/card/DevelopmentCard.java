package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.storage.*;

import java.util.List;

public class DevelopmentCard implements Card {

    private int VP;
    private CardColors color;
    private int level;
    private Production prod;
    private List<Resource> cost;


    DevelopmentCard(){

    }

    @Override
    public Card clone() {
        return null;
    }

    /**
     * Gets the VP amount
     * @return Returns VP
     */
    public int getVP() {
        return VP;
    }

    /**
     * Gets the cost attribute
     * @return Returns cost value
     */
    public List<Resource> getCost(){
        return cost;
    }

    /**
     * Gets the level attribute
     * @return Returns level value
     */
    public int getLevel(){
        return level;
    }

    /**
     * Gets the color attribute
     * @return Returns color value
     */
    public CardColors getColor(){
        return color;
    }

}
