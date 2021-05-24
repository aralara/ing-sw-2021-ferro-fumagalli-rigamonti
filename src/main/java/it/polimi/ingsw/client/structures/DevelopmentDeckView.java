package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;

import java.io.Serializable;

public class DevelopmentDeckView implements Serializable {

    private Deck deck;
    private CardColors deckColor;
    private int deckLevel;


    public DevelopmentDeckView() {
        this.deck = new Deck();
    }

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
