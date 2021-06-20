package it.polimi.ingsw.server.model.cards.deck;

import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles methods relative to the development deck
 */
public class DevelopmentDeck extends Listened implements Serializable {

    private final Deck deck;
    private final CardColors deckColor;
    private final int deckLevel;


    /**
     * Constructor for Deck given a Deck
     * @param deck Deck containing a list of cards
     */
    public DevelopmentDeck(Deck deck) {
        DevelopmentCard firstCard = (DevelopmentCard) deck.get(0);
        this.deckColor = firstCard.getColor();
        this.deckLevel = firstCard.getLevel();
        this.deck = new Deck(createListFromColorLevel(deck));
    }


    /**
     * Creates a list of cards extracting them from a given deck, the cards will have the same cardColor and level
     * as the first DevelopmentCard in the deck
     * @param deck Deck to choose the cards from
     * @return Returns a list of cards
     */
    private static List<Card> createListFromColorLevel(Deck deck) {
        CardColors firstColor = ((DevelopmentCard) deck.get(0)).getColor();
        int firstLevel = ((DevelopmentCard) deck.get(0)).getLevel();
        int[] extractIndexes = new int[4];
        int index = 0;
        for(int i = 0; i < deck.size(); i++) {
            DevelopmentCard dCard = (DevelopmentCard) deck.get(i);
            if (dCard.getColor() == firstColor && dCard.getLevel() == firstLevel)
                extractIndexes[index++] = i;
        }
        List<Card> retList = new ArrayList<>(deck.extract(extractIndexes));
        Collections.shuffle(retList);
        return retList;
    }

    /**
     * Checks if the development deck is empty
     * @return Returns true if the deck has no cards, false otherwise
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Extracts the first card from the DevelopmentDeck
     * @return Returns the first card
     */
    @SuppressWarnings("UnusedReturnValue")
    public DevelopmentCard removeFirst() {
        DevelopmentCard removed = (DevelopmentCard) deck.extract(new int[]{0}).get(0);
        fireUpdate(Listeners.GAME_DEV_DECK.value(), this);
        return removed;
    }

    /**
     * Gets the deck attribute
     * @return Returns deck value
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Gets the deckColor attribute
     * @return Returns deckColor value
     */
    public CardColors getDeckColor() {
        return this.deckColor;
    }

    /**
     * Gets the deckLevel attribute
     * @return Returns deckLevel value
     */
    public int getDeckLevel() {
        return this.deckLevel;
    }
}