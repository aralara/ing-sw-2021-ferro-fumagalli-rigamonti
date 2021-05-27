package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.games.SingleGame;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;

public class LorenzoBoard extends Listened implements Serializable {

    private final SingleGame game;
    private final Deck lorenzoDeck;
    private int faith;


    /**
     * LorenzoBoard constructor given a SingleGame reference
     * @param game Game that will use the board
     */
    public LorenzoBoard(SingleGame game) {
        this.game = game;
        lorenzoDeck = new Deck();
        faith = 0;
    }


    /**
     * Adds a set amount of faith to Lorenzo
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith) {
        this.faith += faith;
        fireUpdate(Listeners.GAME_LORENZO_FAITH.value(), this.faith);
    }

    /**
     * Removes a number of development cards from a development deck of a given color
     * @param color Color of the cards to be removed
     * @param quantity Quantity of cards to be removed
     */
    public void takeDevCard(CardColors color, int quantity) {
        for(int i = 0; i < quantity; i++)
            game.removeDevCard(color, 0);
    }

    /**
     * Initializes lorenzoDeck by loading cards from the specified files and randomizes the order
     * by calling refreshDeck
     * @param fileNameDev File containing the LorenzoDev cards
     * @param fileNameFaith File containing the LorenzoFaith cards
     */
    public void initLorenzoDeck(String fileNameDev, String fileNameFaith) {
        Deck cardsFromFactory;

        cardsFromFactory = new Deck(CardFactory.getInstance().loadLorenzoDevCardsFromFile(fileNameDev));
        for(Card card : cardsFromFactory)
            lorenzoDeck.add(card);

        cardsFromFactory = new Deck(CardFactory.getInstance().loadLorenzoFaithCardsFromFile(fileNameFaith));
        for(Card card : cardsFromFactory)
            lorenzoDeck.add(card);

        refreshDeck();
    }

    /**
     * Picks the Lorenzo card at the top of the deck and moves the picked card to the bottom of the deck
     * @return Returns the selected Lorenzo card
     */
    public LorenzoCard pickLorenzoCard() {
        LorenzoCard extracted = (LorenzoCard) (lorenzoDeck.extract(new int[] {0})).get(0);
        fireUpdate(Listeners.GAME_LORENZO_CARD.value(), extracted);
        lorenzoDeck.add(extracted);
        return extracted;
    }

    /**
     *  Randomizes the order of the Lorenzo cards in the deck
     */
    public void refreshDeck() {
        lorenzoDeck.shuffle();
    }

    /**
     * Gets the lorenzoDeck attribute
     * @return Returns lorenzoDeck value
     */
    public Deck getDeck() {
        return lorenzoDeck;
    }

    /**
     * Gets the faith attribute
     * @return Returns faith value
     */
    public int getFaith() {
        return faith;
    }

}
