package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.utils.exceptions.InvalidSpaceException;
import it.polimi.ingsw.server.model.cards.card.*;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.utils.Constants;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.server.PlayerListened;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles methods to update the development board
 */
public class DevelopmentBoard extends PlayerListened implements Serializable {

    private final List<Deck> spaces;


    /**
     * Default constructor for DevelopmentBoard, initializes a list of empty decks
     */
    public DevelopmentBoard(){
        spaces = new ArrayList<>();
        for(int i = 0; i < Constants.BASE_DEVELOPMENT_SPACES.value(); i++)
            spaces.add(new Deck());
    }


    /**
     * Gets all the productions that can be activated from the top card of each deck
     * @return Returns a list of Production
     */
    public List<Production> getActiveProductions() {
        List<Production> activeProductions = new ArrayList<>();

        for(Deck deck : spaces) {
            if(!deck.isEmpty()) {
                DevelopmentCard developmentCard = (DevelopmentCard) deck.get(0);
                activeProductions.add(developmentCard.getProduction());
            }
        }
        return activeProductions;
    }

    /**
     * Puts a development card at the top of one of the spaces specified by the parameter
     * @param card The development card to be added
     * @param space Position of the space on the board
     * @return Returns true if the card is added correctly
     * @throws InvalidSpaceException When the selected space is out of the allowed range
     */
    public boolean addDevCard(DevelopmentCard card, int space) throws InvalidSpaceException {
        if(space < 0 || space >= Constants.BASE_DEVELOPMENT_SPACES.value()) throw new InvalidSpaceException();
        if((card.getLevel() == 1 && spaces.get(space).isEmpty()) ||
                (!spaces.get(space).isEmpty() &&
                        card.getLevel()-1 == ((DevelopmentCard)spaces.get(space).get(0)).getLevel())) {
                spaces.get(space).addOnTop(card);
                fireUpdate(Listeners.BOARD_DEV_SPACES.value(), spaces);
                return true;
        }
        return false;
    }

    /**
     * Checks if a DevelopmentCard can be placed on the board
     * @param card The development card to be added
     * @param space The space where the card will be added
     * @return Returns true if the card can be added, false otherwise
     */
    public boolean checkDevCardAddable(DevelopmentCard card, int space) {
        Deck deck;
        try {
            deck = spaces.get(space);
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
        return (card.getLevel() == 1 && deck.isEmpty()) ||
                (!deck.isEmpty() &&
                        card.getLevel() - 1 == ((DevelopmentCard) deck.get(0)).getLevel());
    }

    /**
     * Checks if a specified card is present at the right quantity
     * @param color Color of the requested card
     * @param level Level of the requested card, if it's "1" all levels are valid
     * @param number Quantity of cards requested
     * @return Returns true if there are at least the correct quantity, false otherwise
     */
    public boolean checkRequirement(CardColors color, int level, int number) {
        int cardRequested = 0;

        for (Deck deck : spaces)
            for(Card card : deck)
                if ((((DevelopmentCard)card).getColor() == color)
                        && (level == 1 || (((DevelopmentCard)card).getLevel()) == level))
                    cardRequested++;
        return cardRequested >= number;
    }

    /**
     * Gets the total number of cards contained in all the decks of the spaces list
     * @return Returns the number of cards
     */
    public int numberOfCards() {
        int totalNum = 0;

        for(Deck deck : spaces)
            totalNum += deck.size();
        return totalNum;
    }

    /**
     * Calculates total VPs given by the activated development cards for a player
     * @return Returns total VP amount
     */
    public int calculateVP() {
        int vpAmount = 0;

        for(Deck deck : spaces)
            for(Card card : deck)
                vpAmount += ((DevelopmentCard)card).getVP();
        return vpAmount;
    }

    /**
     * Gets the spaces attribute
     * @return Returns spaces value
     */
    public List<Deck> getSpaces() {
        return spaces;
    }
}
