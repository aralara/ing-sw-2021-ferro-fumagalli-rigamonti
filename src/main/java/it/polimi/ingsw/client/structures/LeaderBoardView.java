package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.boards.LeaderBoard;
import it.polimi.ingsw.server.model.cards.card.Card;
import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;
import java.util.stream.Collectors;

public class LeaderBoardView extends Listened implements Serializable {

    private Deck hand;
    private Deck board;


    public LeaderBoardView() {
        this.hand = new Deck();
        this.board = new Deck();
    }

    public LeaderBoardView(LeaderBoard leaderBoard) {
        this.hand = leaderBoard.getHand();
        this.board = leaderBoard.getBoard();
    }


    /**
     * Gets the hand attribute
     * @return Returns hand
     */
    public Deck getHand() {
        return hand;
    }

    /**
     * Sets the hand attribute
     * @param hand New attribute value
     */
    public void setHand(Deck hand) {
        this.hand = hand;
        if(hasListeners())
            fireUpdate(Listeners.BOARD_LEADER_HAND.value(), hand.getCards().stream()
                    .map(Card::getID).collect(Collectors.toList()));
    }

    /**
     * Gets the board attribute
     * @return Returns board
     */
    public Deck getBoard() {
        return board;
    }

    /**
     * Sets the board attribute
     * @param board New attribute value
     */
    public void setBoard(Deck board) {
        this.board = board;
        if(hasListeners())
            fireUpdate(Listeners.BOARD_LEADER_BOARD.value(), board.getCards().stream()
                    .map(Card::getID).collect(Collectors.toList()));
    }
}
