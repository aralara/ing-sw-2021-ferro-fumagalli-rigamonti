package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.storage.*;

import java.util.List;

public class DevelopmentCard implements Card {

    private int VP;
    private CardColors color;
    private int level;
    private Production production;
    private List<Resource> cost;


    DevelopmentCard() {

    }

    DevelopmentCard(DevelopmentCard card) {
        this.VP = card.getVP();
        this.color = card.getColor();
        this.level = card.getLevel();
        this.production = card.getProduction();
        this.cost = card.getCost();
        //TODO: Valutare se far restituire degli oggetti copia ai getter e implementare dei metodi per restituire copie di Production e Resource
    }


    @Override
    public DevelopmentCard makeClone() {
        return new DevelopmentCard(this);
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
