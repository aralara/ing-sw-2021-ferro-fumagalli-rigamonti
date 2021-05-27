package it.polimi.ingsw.server.model.cards.card;

import java.io.Serializable;

public abstract class Card implements Serializable {

    private int ID;

    @Override
    public abstract String toString();

    /**
     * Gets the ID attribute
     * @return Returns ID value
     */
    public int getID() {
        return ID;
    }

    /**
     * Sets the ID attribute
     * @param ID New ID value
     */
    public void setID(int ID) {
        this.ID = ID;
    }

}
