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
        spaces.get(space).addOnTop(card);
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

    /**
     * Checks if a specified card is present at the right quantity
     * @param color Color of the requested card
     * @param level Level of the requested card
     * @param number Quantity of cards requested
     * @return Returns true if there are at least the correct quantity, false otherwise
     */
    public boolean checkRequirement(CardColors color, int level, int number) {
        int cardRequested=0;

        for (Deck deck : spaces)
            for(Card card : deck)
                if ((((DevelopmentCard)card).getColor() == color)
                        && (((DevelopmentCard)card).getLevel()) == level)
                    cardRequested++;

        return cardRequested >= number;
    }

    /**
     * Gets the total number of cards contained in all the decks of the spaces list
     * @return Returns the number of cards
     */
    public int numberOfCards(){
        int totalNum = 0;

        for(Deck deck : spaces)
            totalNum += deck.size();

        return totalNum;
    }

    /**
     * Calculates total VPs given by the activated development cards for a player
     * @return Returns total VP amount
     */
    public int calculateVP(){
        int vpAmount = 0;

        for(Deck deck : spaces)
            for(Card card : deck)
                vpAmount += ((DevelopmentCard)card).getVP();

        return vpAmount;
    }
}
