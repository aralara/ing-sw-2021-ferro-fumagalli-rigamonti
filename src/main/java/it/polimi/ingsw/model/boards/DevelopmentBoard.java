package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.*;
import it.polimi.ingsw.model.cards.deck.Deck;
import it.polimi.ingsw.model.storage.Production;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentBoard {

    private List<Deck> spaces;


    public DevelopmentBoard(){

    }


    public List<Card> getAllCards() {
        List<Card> temp = new ArrayList<>();
        for (Deck space : spaces) {
            temp.addAll(space.getCards());
        }
        return temp;
    }

    /**
     * Gets all the productions that can be activated from the top card of each deck
     * @return Returns a list of Production
     */
    public List<Production> getActiveProductions() {
        return null;
    }

    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     */
    public void addDevCard(DevelopmentCard card, int space) {

    }

    /**
     * Checks if a DevelopmentCard can be placed on the board
     * @param card The development card to be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean checkDevCardAddable(DevelopmentCard card) {
        return false;
    }
}
