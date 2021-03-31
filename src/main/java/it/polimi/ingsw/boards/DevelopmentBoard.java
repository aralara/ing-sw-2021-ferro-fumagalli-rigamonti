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
    public void addDevCard(DevelopmentCard card, int space) {

    }

    /**
     * Checks if a DevelopmentCard can be placed in a specific space on the board
     * @param card The development card to be added
     * @param space Position of the space on the board
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean checkDevCardAddable(DevelopmentCard card, int space) {
        return false;
    }
}
