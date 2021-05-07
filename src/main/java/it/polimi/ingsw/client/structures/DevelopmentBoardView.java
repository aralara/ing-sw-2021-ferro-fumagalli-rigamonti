package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.deck.Deck;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentBoardView {

    private static final int NUMBER_OF_SPACES = 3;

    private List<Deck> spaces;


    public DevelopmentBoardView(List<Deck> spaces){
        this.spaces = new ArrayList<>();
        this.spaces = spaces;
    }


    /**
     * Gets the spaces attribute
     * @return Returns spaces
     */
    public List<Deck> getSpaces() {
        return spaces;
    }

    /**
     * Sets the spaces attribute
     * @param spaces New attribute value
     */
    public void setSpaces(List<Deck> spaces) {
        this.spaces = spaces;
    }
}
