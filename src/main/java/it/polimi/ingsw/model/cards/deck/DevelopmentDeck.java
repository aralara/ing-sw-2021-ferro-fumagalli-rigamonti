package it.polimi.ingsw.model.cards.deck;

import it.polimi.ingsw.model.cards.card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevelopmentDeck {

    private Deck deck;
    private CardColors deckColor;
    private int deckLevel;


    public DevelopmentDeck(Deck deck) {
        this.deck = new Deck(createListFromColorLevel(deck));
        DevelopmentCard firstCard = (DevelopmentCard) deck.get(0);
        this.deckColor = firstCard.getColor();
        this.deckLevel = firstCard.getLevel();
    }


    /**
     * Creates a list of cards extracting them from a given deck, the cards will have the same cardColor and level
     * as the first DevelopmentCard in the deck
     * @param deck Deck to choose the cards from
     * @return Returns a list of cards
     */
    private static List<Card> createListFromColorLevel(Deck deck) {
        List<Card> retList = new ArrayList<>();
        CardColors firstColor = ((DevelopmentCard)deck.get(0)).getColor();
        int firstLevel = ((DevelopmentCard)deck.get(0)).getLevel();
        for(Card card : deck) {
            DevelopmentCard dCard = (DevelopmentCard)card;
            if (dCard.getColor() == firstColor && dCard.getLevel() == firstLevel)   //TODO: Può essere semplificato implementando un equals() in DevelopmentCard
                retList.add(deck.extract( new int[]{deck.indexOf(card)} ).get(0));
        }
        Collections.shuffle(retList);
        return retList;
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
    public DevelopmentCard removeFirst() {
        return (DevelopmentCard) deck.extract(new int[]{0});
    }
}