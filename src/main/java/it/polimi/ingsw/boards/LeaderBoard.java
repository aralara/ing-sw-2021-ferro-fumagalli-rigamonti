package it.polimi.ingsw.boards;

import it.polimi.ingsw.cards.deck.Deck;
import it.polimi.ingsw.cards.ability.SpecialAbility;

import java.util.List;

public class LeaderBoard {

    private Deck hand;
    private Deck board;


    public LeaderBoard() {

    }


    /**
     * Gives to each player a hand of leader cards
     * @param hand Deck of leader cards
     */
    public void setLeaderHand(Deck hand) {

    }

    /**
     * Method invoked to discard a leader card from the hand specifying its position
     * @param position Position of the card to be discarded
     */
    public void discardLeaderHand(int position) {

    }

    /**
     * Puts a LeaderCard from the hand to the board
     * @param position Position of the card to play
     */
    public void playLeaderCard(int position){

    }

    /**
     * Gets all the active leader abilities
     * @return Returns a list of SpecialAbility
     */
    public List<SpecialAbility> getLeaderAbilities() {
        return null;
    }
}
