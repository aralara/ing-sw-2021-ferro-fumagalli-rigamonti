package it.polimi.ingsw.server.model.cards.card;

import java.io.Serializable;

public abstract class Card implements Serializable {

    private int ID;

    /**
     * Transform a card into a string to be printed
     * @return A string that contains card information
     */
    public abstract String cardToString();

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
