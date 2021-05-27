package it.polimi.ingsw.server.model.cards.deck;

import it.polimi.ingsw.server.model.cards.card.Card;

import java.io.Serializable;
import java.util.*;
import java.util.stream.IntStream;

public class Deck implements Iterable<Card>, Serializable {

    private final List<Card> cards;


    /**
     * Default Deck constructor
     */
    public Deck(){
        cards = new ArrayList<>();
    }

    /**
     * Constructor for Deck given a list of cards
     * @param cards List of cards to make a deck
     */
    public Deck(List<? extends Card> cards) {
        this.cards = new ArrayList<>(cards);
    }


    /**
     * Checks if the deck is empty
     * @return Returns true if the deck has no cards, false otherwise
     */
    public boolean isEmpty() {
        return cards.size() <= 0;
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
     * Gets the index of a specified card
     * @param card Card to get the index
     * @return Returns the index of the card
     */
    public int indexOf(Card card) {
        return IntStream.range(0, cards.size())
                .filter(i -> cards.get(i).getID() == card.getID()).findFirst().orElse(-1);
    }

    /**
     * Removes a group of cards from the deck given their positions and return them as a list
     * @param positions Positions of the cards to extract
     * @return Returns a list of the selected cards
     */
    public List<Card> extract(int[] positions) {
        List<Card> retList = new ArrayList<>();
        for (int position : positions)
            retList.add(cards.get(position));
        for(Card card : retList)
            cards.remove(card);
        return retList;
    }

    /**
     * Adds a card on top of the deck
     * @param card Card object to add
     */
    public void addOnTop(Card card) {
        cards.add(0, card);
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

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }

    /**
     * Gets the cards attribute
     * @return Returns cards value
     */
    public List<Card> getCards() {
        return cards;
    }
}