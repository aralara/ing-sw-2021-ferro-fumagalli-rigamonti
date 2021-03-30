package it.polimi.ingsw;

import java.util.List;

public class Deck {

    private List<Card> cards;


    public Deck() {

    }


    /**
     * Gets a card specified by the given position
     * @param position Position of the card
     * @return Returns the requested card
     */
    public Card get(int position) {
        return null;
    }

    /**
     * Gets the cards attribute
     * @return Returns cards value
     */
    public List<Card> getCards() {
        return null;
    }

    /**
     * Removes a group of cards from the deck given their positions and return them as a list
     * @param positions Positions of the cards to extract
     * @return Returns a list of the selected cards
     */
    public List<Card> extract(int[] positions) {
        return null;
    }

    /**
     * Adds a card to the deck in the first free position
     * @param card Card object to add
     */
    public void add(Card card) {

    }

    /**
     * Gets the length of the deck
     * @return Returns size value
     */
    public int size() {
        return -1;
    }

    /**
     * Randomizes the cards' positions in the deck
     */
    public void shuffle() {

    }
}