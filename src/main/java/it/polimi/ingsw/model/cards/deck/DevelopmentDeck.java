package it.polimi.ingsw.model.cards.deck;

import it.polimi.ingsw.model.cards.card.*;

import java.util.List;

public class DevelopmentDeck extends Deck{

    private CardColors deckColor;
    private int deckLevel;


    public DevelopmentDeck() {

    }


    /**
     * Creates a list of cards extracting them from a given deck, the cards will have the same CardColors and level
     * as the first DevelopmentCard in the deck
     * @param deck Deck to choose the cards from
     * @return Returns a list of cards
     */
    public List<Card> createFromColorLevel(Deck deck) {
        return null;
    }

    /**
     * Extracts the first card from the DevelopmentDeck
     * @return Returns the first card
     */
    public DevelopmentCard removeFirst() {
        return null;
    }
}
