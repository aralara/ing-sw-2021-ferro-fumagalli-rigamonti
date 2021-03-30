package it.polimi.ingsw.boards;

import it.polimi.ingsw.cards.deck.Deck;
import it.polimi.ingsw.games.Game;
import it.polimi.ingsw.cards.card.LorenzoCard;

public class LorenzoBoard {

    private Game game;
    private Deck lorenzoDeck;


    public LorenzoBoard(){

    }


    /**
     * Adds a set amount of faith to Lorenzo
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith){

    }

    /**
     * Initializes lorenzoDeck by loading cards from the specified file and randomizes the order by calling refreshDeck
     */
    public void initLorenzoDeck(){

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
