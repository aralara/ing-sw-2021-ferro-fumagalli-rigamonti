package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.storage.*;

import java.util.List;

public class DevelopmentCard extends Card {

    private final int VP;
    private final CardColors color;
    private final int level;
    private final Production production;
    private final List<Resource> cost;


    /**
     * Default constructor for a DevelopmentCard having ID = -1 which is used to denote a hidden card
     */
    public DevelopmentCard() {
        setID(-1);
        this.VP = 0;
        this.color = null;
        this.level = -1;
        this.production = null;
        this.cost = null;
    }

    /**
     * Constructor for a DevelopmentCard card
     * @param ID Unique ID reference for the card
     * @param VP Victory points given by the card
     * @param color Color of the card
     * @param level Level of the card
     * @param production Production given by the card
     * @param cost Cost of the card
     */
    public DevelopmentCard(int ID, int VP, CardColors color, int level, Production production, List<Resource> cost) {
        setID(ID);
        this.VP = VP;
        this.color = color;
        this.level = level;
        this.production = production;
        this.cost = cost;
    }


    @Override
    public String cardToString() {
        boolean first = true;
        StringBuilder toPrint;
        toPrint = new StringBuilder(" DEVELOPMENT CARD \n • This is a " + color + " card level " + level +
                "\n • Victory points: " + VP + "\n • Cost: ");
        for (Resource resource : cost) {
            toPrint.append((!first) ? ", " : "").append(resource.getQuantity()).append(" ").append(resource.getResourceType());
            first = false;
        }

        toPrint.append("\n • Production that can be activated:\n ").append(production.productionToPrint());
        return toPrint.toString();
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
