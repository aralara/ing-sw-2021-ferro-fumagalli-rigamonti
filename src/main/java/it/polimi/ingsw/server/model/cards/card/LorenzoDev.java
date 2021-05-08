package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.boards.LorenzoBoard;

public class LorenzoDev implements LorenzoCard{

    private final CardColors color;
    private final int quantity,ID;


    public LorenzoDev(int ID, CardColors color, int quantity) {
        this.ID = ID;
        this.color = color;
        this.quantity = quantity;
    }


    /**
     * Gets the ID value
     * @return Returns ID
     */
    public int getID() {
        return ID;
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
