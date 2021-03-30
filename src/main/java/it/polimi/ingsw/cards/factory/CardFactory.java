package it.polimi.ingsw.cards.factory;

import it.polimi.ingsw.cards.card.Card;

import java.util.List;

public interface CardFactory {

    /**
     * Loads a list of cards from a specified file
     * @param fileName Name of the file to load the cards from
     * @return Returns the list of loaded cards
     */
    List<Card> loadCardFromFile(String fileName);
}
