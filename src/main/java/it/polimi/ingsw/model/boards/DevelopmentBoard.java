package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.*;
import it.polimi.ingsw.model.cards.deck.Deck;
import it.polimi.ingsw.model.storage.Production;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentBoard {

    private List<Deck> spaces;


    public DevelopmentBoard(){
        spaces = new ArrayList<>();
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
        List<Production> activeProductions = new ArrayList<>();

        for(Deck deck : spaces){
            DevelopmentCard developmentCard = (DevelopmentCard) deck.get(0);
            activeProductions.add(developmentCard.getProduction());
        }

        return activeProductions;
    }

    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     */
    public void addDevCard(DevelopmentCard card, int space) {
        if(spaces.get(space).isEmpty())
            spaces.get(space).add(card);
        else {
            Deck toChange = new Deck();
            toChange.add(card);
            int deckSize = spaces.get(space).size();
            for (int i=0; i<deckSize; i++){
                toChange.add(spaces.get(space).extract(new int[] {0}).get(0));
            }
            for(Card cardToAdd : toChange)
                spaces.get(space).add(cardToAdd);
        }
    }

    /**
     * Checks if a DevelopmentCard can be placed on the board
     * @param card The development card to be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean checkDevCardAddable(DevelopmentCard card) {
        if(card.getLevel()==1) {
            for (Deck deck : spaces)
                if (deck.isEmpty())
                    return true;
            return false;
        } else if(card.getLevel()==2) {
            for (Deck deck : spaces)
                if (!deck.isEmpty() && ((DevelopmentCard)deck.get(0)).getLevel()==1)
                    return true;
            return false;
        } else {
            for (Deck deck : spaces)
                if (!deck.isEmpty() && ((DevelopmentCard)deck.get(0)).getLevel()==2)
                    return true;
            return false;
        }
    }
}
