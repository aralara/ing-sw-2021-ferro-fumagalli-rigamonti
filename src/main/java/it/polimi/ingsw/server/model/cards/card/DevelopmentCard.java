package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.storage.*;

import java.util.List;

public class DevelopmentCard implements Card {

    private final int ID;
    private final int VP;
    private final CardColors color;
    private final int level;
    private final Production production;
    private final List<Resource> cost;


    public DevelopmentCard() {
        this.ID = -1;
        this.VP = 0;
        this.color = null;
        this.level = -1;
        this.production = null;
        this.cost = null;
    }

    public DevelopmentCard(int ID, int VP, CardColors color, int level, Production production, List<Resource> cost) {
        this.ID = ID;
        this.VP = VP;
        this.color = color;
        this.level = level;
        this.production = production;
        this.cost = cost;
    }


    /**
     * Gets the ID value
     * @return Returns ID
     */
    public int getID() {
        return ID;
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
