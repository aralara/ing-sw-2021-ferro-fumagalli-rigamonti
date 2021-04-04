package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.LeaderCard;
import it.polimi.ingsw.model.cards.deck.Deck;

import java.util.List;

public class LeaderBoard {

    private Deck hand;
    private Deck board;


    public LeaderBoard() {

    }


    /**
     * Gives a hand of leader cards to the player
     * @param hand List of leader cards
     */
    public void setLeaderHand(List<LeaderCard> hand) {

    }

    /**
     * Method invoked to discard a leader card from the hand specifying the card
     * @param leaderCard LeaderCard to be discarded
     */
    public void discardLeaderHand(LeaderCard leaderCard) {

    }

    /**
     * Puts a LeaderCard from the hand to the board
     * @param leaderCard LeaderCard to be played
     */
    public void playLeaderHand(LeaderCard leaderCard){

    }
}
