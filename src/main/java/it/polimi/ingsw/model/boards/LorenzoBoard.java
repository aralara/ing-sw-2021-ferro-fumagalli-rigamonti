package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.*;
import it.polimi.ingsw.model.cards.deck.Deck;
import it.polimi.ingsw.model.cards.factory.LorenzoDevFactory;
import it.polimi.ingsw.model.cards.factory.LorenzoFaithFactory;
import it.polimi.ingsw.model.games.Game;

public class LorenzoBoard {

    private Game game;
    private Deck lorenzoDeck;
    private int faith;


    public LorenzoBoard(Game game) {
        this.game = game;
        lorenzoDeck = new Deck();
        faith = 0;
    }


    /**
     * Gets the faith attribute
     * @return Returns faith value
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Adds a set amount of faith to Lorenzo
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith) {
        this.faith += faith;
    }

    /**
     * Removes a number of development cards from a development deck of a given color
     * @param color Color of the cards to be removed
     * @param quantity Quantity of cards to be removed
     */
    public void takeDevCard(CardColors color, int quantity) {
        for(int i=0; i<quantity; i++)
            game.removeDevCard(color, 0);
            //TODO: if(!(game.removeDevCard(color, 0))) lanciare eccezione?
    }

    /**
     * Initializes lorenzoDeck by loading cards from the specified files and randomizes the order
     * by calling refreshDeck
     * @param fileNameDev File containing the LorenzoDev cards
     * @param fileNameFaith File containing the LorenzoFaith cards
     */
    public void initLorenzoDeck(String fileNameDev, String fileNameFaith) {
        LorenzoDevFactory lorenzoDevFactory = new LorenzoDevFactory();
        LorenzoFaithFactory lorenzoFaithFactory = new LorenzoFaithFactory();
        Deck cardsFromFactory;

        cardsFromFactory = new Deck(lorenzoDevFactory.loadCardFromFile(fileNameDev));
        for(Card card : cardsFromFactory){
            lorenzoDeck.add(card);
        }

        cardsFromFactory = new Deck(lorenzoFaithFactory.loadCardFromFile(fileNameFaith));
        for(Card card : cardsFromFactory){
            lorenzoDeck.add(card);
        }

        refreshDeck();
    }

    /**
     * Picks the Lorenzo card at the top of the deck and moves the picked card to the bottom of the deck
     * @return Returns the selected Lorenzo card
     */
    public LorenzoCard pickLorenzoCard() {
        LorenzoCard extracted = (LorenzoCard) (lorenzoDeck.extract(new int[] {0})).get(0);
        lorenzoDeck.add(extracted);
        return extracted;
    }

    /**
     *  Randomizes the order of the Lorenzo cards in the deck
     */
    public void refreshDeck() {
        lorenzoDeck.shuffle();
    }
}
