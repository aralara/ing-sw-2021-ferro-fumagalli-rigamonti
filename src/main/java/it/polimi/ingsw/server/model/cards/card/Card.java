package it.polimi.ingsw.server.model.cards.card;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines a class to handle common parts of all cards
 */
public abstract class Card implements Serializable {

    private int ID;

    @Override
    public abstract String toString();

    @Override
    @SuppressWarnings("RedundantIfStatement")
    public boolean equals(Object obj) {                   //TODO: rendere gli ID unici
        if(obj == null || this.getClass() != obj.getClass()) return false;
        final Card card = (Card) obj;
        if(this.ID != card.ID) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

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
