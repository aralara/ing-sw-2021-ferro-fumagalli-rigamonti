package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.boards.DevelopmentBoard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DevelopmentBoardView implements Serializable {

    private List<Deck> spaces;


    public DevelopmentBoardView() {
        this.spaces = new ArrayList<>();
        for(int i = 0; i < Constants.BASE_DEVELOPMENT_SPACES.value(); i++)
            spaces.add(new Deck());
    }

    public DevelopmentBoardView(DevelopmentBoard developmentBoard) {
        this.spaces = developmentBoard.getSpaces();
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
