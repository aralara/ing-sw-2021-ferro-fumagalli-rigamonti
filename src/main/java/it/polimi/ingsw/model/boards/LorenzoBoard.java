package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.CardColors;
import it.polimi.ingsw.model.cards.deck.Deck;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.cards.card.LorenzoCard;

public class LorenzoBoard {

    private Game game;
    private Deck lorenzoDeck;
    private int faith;


    public LorenzoBoard(){

    }


    /**
     * Gets the faith attribute
     * @return Returns faith value
     */
    public int getFaith(){
        return faith;
    }

    /**
     * Adds a set amount of faith to Lorenzo
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith){

    }

    /**
     * Removes a number of development cards from a development deck of a given color
     * @param color Color of the cards to be removed
     * @param quantity Quantity of cards to be removed
     */
    public void takeDevCard(CardColors color, int quantity){

    }

    /**
     * Initializes lorenzoDeck by loading cards from the specified files and randomizes the order by calling refreshDeck
     * @param fileNameDev File containing the LorenzoDev cards
     * @param fileNameFaith File containing the LorenzoFaith cards
     */
    public void initLorenzoDeck(String fileNameDev, String fileNameFaith){

    }

    /**
     * Picks the Lorenzo card at the top of the deck and moves the picked card to the bottom of the deck
     * @return Returns the selected Lorenzo card
     */
    public LorenzoCard pickLorenzoCard(){
        return null;
    }

    /**
     *  Randomizes the order of the Lorenzo cards in the deck
     */
    public void refreshDeck(){

    }
}
