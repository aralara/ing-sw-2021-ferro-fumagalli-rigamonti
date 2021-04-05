package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.cards.card.LeaderCard;
import it.polimi.ingsw.model.cards.deck.Deck;

import java.util.List;

public class LeaderBoard {

    private Deck hand;
    private Deck board;


    public LeaderBoard() {
        hand = new Deck();
        board = new Deck();
    }


    /**
     * Gives a hand of leader cards to the player
     * @param hand List of leader cards
     */
    public void setLeaderHand(List<LeaderCard> hand) {
        for(LeaderCard leaderCard : hand)
            this.hand.add(leaderCard);
    }

    /**
     * Method invoked to discard a leader card from the hand specifying the card
     * @param leaderCard LeaderCard to be discarded
     */
    public void discardLeaderHand(LeaderCard leaderCard) {
        int index = hand.indexOf(leaderCard);
        hand.extract(new int[] {index});
    }

    /**
     * Puts a LeaderCard from the hand to the board
     * @param leaderCard LeaderCard to be played
     */
    public void playLeaderHand(LeaderCard leaderCard){
        int index = hand.indexOf(leaderCard);
        board.add(hand.extract(new int[] {index}).get(0));
    }
}
