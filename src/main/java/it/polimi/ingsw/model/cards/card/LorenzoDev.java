package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public class LorenzoDev implements LorenzoCard{

    private CardColors color;
    private int quantity;


    public LorenzoDev(CardColors color, int quantity) {
        this.color = color;
        this.quantity = quantity;
    }

    private LorenzoDev(LorenzoDev card) {
        this.color = card.getColor();
        this.quantity = card.getQuantity();
    }


    @Override
    public void activateLorenzo(LorenzoBoard board) {
        board.takeDevCard(color, quantity);
    }

    @Override
    public LorenzoDev makeClone() {
        return new LorenzoDev(this);
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
