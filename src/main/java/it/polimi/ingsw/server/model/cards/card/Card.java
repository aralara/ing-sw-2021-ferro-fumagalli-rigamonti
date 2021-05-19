package it.polimi.ingsw.server.model.cards.card;

import java.io.Serializable;

public interface Card extends Serializable {

    int getID();

    /**
     * Transform a card into a string to be printed
     * @return A string that contains card information
     */
    String cardToString();
}
