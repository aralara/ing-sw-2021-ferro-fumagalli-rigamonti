package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.boards.LorenzoBoard;

public class LorenzoDev extends LorenzoCard {

    private final CardColors color;
    private final int quantity;


    /**
     * Constructor for a LorenzoDev card
     * @param ID Unique ID reference for the card
     * @param color Color of the development cards to discard
     * @param quantity Quantity of the development cards to discard
     */
    public LorenzoDev(int ID, CardColors color, int quantity) {
        setID(ID);
        this.color = color;
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "Lorenzo removes " + quantity + " " +color +" development cards from the development decks \n";
    }

    @Override
    public void activateLorenzo(LorenzoBoard board) {
        board.takeDevCard(color, quantity);
    }

    /**
     * Gets the color attribute
     * @return Returns color value
     */
    public CardColors getColor() {
        return color;
    }

    /**
     * Gets the quantity attribute
     * @return Returns quantity value
     */
    public int getQuantity() {
        return quantity;
    }
}
