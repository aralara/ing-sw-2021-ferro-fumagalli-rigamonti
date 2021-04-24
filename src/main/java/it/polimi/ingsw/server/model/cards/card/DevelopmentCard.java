package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.storage.*;

import java.util.List;

public class DevelopmentCard implements Card {

    private int VP;
    private CardColors color;
    private int level;
    private Production production;
    private List<Resource> cost;


    public DevelopmentCard(int VP, CardColors color, int level, Production production, List<Resource> cost) {
        this.VP = VP;
        this.color = color;
        this.level = level;
        this.production = production;
        this.cost = cost;
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
    public List<Resource> getCost() {
        return cost;
    }

    /**
     * Gets the level attribute
     * @return Returns level value
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the color attribute
     * @return Returns color value
     */
    public CardColors getColor() {
        return color;
    }

    /**
     * Gets the production attribute
     * @return Returns production value
     */
    public Production getProduction() {
        return production;
    }

}
