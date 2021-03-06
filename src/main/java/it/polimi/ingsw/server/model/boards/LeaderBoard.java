package it.polimi.ingsw.server.model.boards;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.listeners.Listeners;
import it.polimi.ingsw.utils.listeners.server.PlayerListened;

import java.io.Serializable;
import java.util.List;

/**
 * Handles methods to update the leader board
 */
public class LeaderBoard extends PlayerListened implements Serializable {

    private final Deck hand;
    private final Deck board;


    /**
     * Default constructor for LeaderBoard
     */
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
        fireUpdate(Listeners.BOARD_LEADER_HAND.value(), hand);
    }

    /**
     * Puts a LeaderCard from the hand to the board
     * @param leaderCard LeaderCard to be played
     */
    public void playLeaderHand(LeaderCard leaderCard) {
        int index = hand.indexOf(leaderCard);
        board.add(hand.extract(new int[] {index}).get(0));
        fireUpdate(Listeners.BOARD_LEADER_HAND.value(), hand);
        fireUpdate(Listeners.BOARD_LEADER_BOARD.value(), board);
    }

    /**
     * Calculates total VPs given by the activated leader cards for a player
     * @return Returns total VP amount
     */
    public int calculateVP() {
        int vpAmount = 0;
        for(int i = 0; i < board.size(); i++)
            vpAmount += ((LeaderCard)board.get(i)).getVP();
        return vpAmount;
    }

    /**
     * Gets the hand attribute
     * @return Returns hand value
     */
    public Deck getHand() {
        return hand;
    }

    /**
     * Gets the board attribute
     * @return Returns board value
     */
    public Deck getBoard() {
        return board;
    }
}
