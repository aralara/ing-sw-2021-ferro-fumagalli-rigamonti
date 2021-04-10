package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public class LorenzoDev implements LorenzoCard{

    private CardColors color;
    private int quantity;


    public LorenzoDev(CardColors color, int quantity) {
        this.color = color;
        this.quantity = quantity;
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
