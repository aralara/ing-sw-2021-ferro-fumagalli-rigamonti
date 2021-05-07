package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.card.CardColors;
import it.polimi.ingsw.server.model.cards.deck.Deck;

public class DevelopmentDeckView {

    private Deck deck;
    private CardColors deckColor;
    private int deckLevel;

    public DevelopmentDeckView(){
        this.deck = new Deck();
    }

    public DevelopmentDeckView(Deck deck, CardColors deckColor, int deckLevel){
        this.deck = deck;
        this.deckColor = deckColor;
        this.deckLevel = deckLevel;
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
     * Sets the deckColor attribute
     * @param deckColor New attribute value
     */
    public void setDeckColor(CardColors deckColor) {
        this.deckColor = deckColor;
    }

    /**
     * Gets the deckLevel attribute
     * @return Returns deckLevel
     */
    public int getDeckLevel() {
        return deckLevel;
    }

    /**
     * Sets the deckLevel attribute
     * @param deckLevel New attribute value
     */
    public void setDeckLevel(int deckLevel) {
        this.deckLevel = deckLevel;
    }
}
