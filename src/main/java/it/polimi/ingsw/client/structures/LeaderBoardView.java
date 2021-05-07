package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.deck.Deck;

public class LeaderBoardView {

    private Deck hand;
    private Deck board;


    public LeaderBoardView() {
        this.hand = new Deck();
        this.board = new Deck();
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
    }
}
