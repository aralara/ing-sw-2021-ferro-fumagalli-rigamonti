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
        setSpaces(spaces);
    }


    /**
     * Gets the spaces attribute
     * @return Returns spaces
     */
    public List<Deck> getSpaces() {
        return spaces; //TODO: va bene o creare copia?
    }

    /**
     * Sets the spaces attribute
     * @param spaces New attribute value
     */
    public void setSpaces(List<Deck> spaces) {  //TODO: aggiunge carte ai deck presenti, da usare sempre su nuovo oggetto
        for(int i=0; i<NUMBER_OF_SPACES; i++)
            for(Card card : spaces.get(i).getCards())
                this.spaces.get(i).add(card);
    }
}
