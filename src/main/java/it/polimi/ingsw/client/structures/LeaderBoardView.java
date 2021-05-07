package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.cards.card.Card; //TODO:duplicare anche card e deck o spostare in package condiviso?
import it.polimi.ingsw.server.model.cards.deck.Deck;

public class LeaderBoardView {

    private Deck hand;
    private Deck board;


    public LeaderBoardView(Deck hand, Deck board) {
        this.hand = new Deck();
        this.board = new Deck();
        setHand(hand);
        setBoard(board);
    }


    /**
     * Gets the hand attribute
     * @return Returns hand
     */
    public Deck getHand() {
        Deck handCopy = new Deck();
        for(Card card : hand)
            handCopy.add(card);
        return handCopy;
    }

    /**
     * Sets the hand attribute
     * @param hand New attribute value
     */
    public void setHand(Deck hand) {
        for(Card card : hand)
            this.hand.add(card);
    }

    /**
     * Gets the board attribute
     * @return Returns board
     */
    public Deck getBoard() {
        Deck boardCopy = new Deck();
        for(Card card : board)
            boardCopy.add(card);
        return boardCopy;
    }

    /**
     * Sets the board attribute
     * @param board New attribute value
     */
    public void setBoard(Deck board) {
        for(Card card : board)
            this.board.add(card);
    }
}
