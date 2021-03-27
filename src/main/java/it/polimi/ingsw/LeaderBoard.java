package it.polimi.ingsw;

import java.util.List;

public class LeaderBoard {

    private Deck hand;
    private Deck board;


    public LeaderBoard(){

    }


    /**
     * Gives to each player a hand of leader cards
     * @param hand Deck of leader cards
     */
    public void setLeadHand(Deck hand){

    }

    /**
     * Method invoked to discard a given number of leader cards from the hand specifying the position of each card
     * @param position Positions of the cards to be discarded
     */
    public void discardLeadHand(int[] position){

    }

    public List<SpecialAbility> getLeadAbilities(){
        return null;
    }
}
