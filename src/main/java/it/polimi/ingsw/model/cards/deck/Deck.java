package it.polimi.ingsw.model.cards.deck;

import it.polimi.ingsw.model.cards.card.Card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards;


    public Deck(){

    }

    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards.size());
        for(Card card : cards)
            this.cards.add(card.clone());
    }


    /**
     * Gets a card specified by the given position
     * @param position Position of the card
     * @return Returns the requested card
     */
    public Card get(int position) {
        return cards.get(position);
    }

    /**
     * Gets the cards attribute
     * @return Returns cards value
     */

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Removes a group of cards from the deck given their positions and return them as a list
     * @param positions Positions of the cards to extract
     * @return Returns a list of the selected cards
     */
    public List<Card> extract(int[] positions) {
        List<Card> retList = new ArrayList<>();
        for(int i : positions)
            retList.add(cards.remove(i));
        return retList;
    }

    /**
     * Adds a card to the deck in the first free position
     * @param card Card object to add
     */
    public void add(Card card) {
        cards.add(card);
    }

    /**
     * Gets the length of the deck
     * @return Returns size value
     */
    public int size() {
        return cards.size();
    }

    /**
     * Randomizes the cards' positions in the deck
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }
}