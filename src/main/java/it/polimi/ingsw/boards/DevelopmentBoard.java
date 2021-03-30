package it.polimi.ingsw.boards;

import it.polimi.ingsw.cards.deck.Deck;
import it.polimi.ingsw.cards.card.DevelopmentCard;

import java.util.List;

public class DevelopmentBoard {

    private List<Deck> spaces;


    public DevelopmentBoard(){

    }


    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     */
    public void addDevCard(DevelopmentCard card, int space){

    }

    /**
     * Gets a parallel array that contains the level of the development cards that can be added to each space
     * @return Returns the array of possible levels
     */
    public int[] getPossibleLevels(){
        return null;
    }
}
