package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;

/**
 * Handles developmentDeck client methods
 */
public class DevelopmentDeckView extends Listened implements Serializable {

    private Deck deck;
    private final CardColors deckColor;
    private final int deckLevel;


    /**
     * DevelopmentDeckView constructor with parameters
     * @param deck Deck to set
     */
    public DevelopmentDeckView(DevelopmentDeck deck) {
        this.deck = deck.getDeck();
        this.deckColor = deck.getDeckColor();
        this.deckLevel = deck.getDeckLevel();
    }


    /**
     * Gets the deck attribute
     * @return Returns deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Sets the deck attribute
     * @param deck New attribute value
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
        if(hasListeners())
            fireUpdate(Listeners.GAME_DEV_DECK.value(), deck);
    }

    /**
     * Gets the deckColor attribute
     * @return Returns deckColor
     */
    public CardColors getDeckColor() {
        return deckColor;
    }

    /**
     * Gets the deckLevel attribute
     * @return Returns deckLevel
     */
    public int getDeckLevel() {
        return deckLevel;
    }
}
