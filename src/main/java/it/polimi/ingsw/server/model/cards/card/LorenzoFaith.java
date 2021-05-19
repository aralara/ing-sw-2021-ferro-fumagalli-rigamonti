package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.boards.LorenzoBoard;

public class LorenzoFaith implements LorenzoCard{

    private final boolean refresh;
    private final int amount, ID;


    public LorenzoFaith(int ID, boolean refresh, int amount) {
        this.ID = ID;
        this.refresh = refresh;
        this.amount = amount;
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
        board.addFaith(amount);
        if(refresh)
            board.refreshDeck();
    }

    /**
     * Gets if the refresh attribute is active
     * @return Returns refresh value
     */
    public boolean isRefresh() {
        return refresh;
    }

    /**
     * Gets the amount attribute
     * @return Returns amount value
     */
    public int getAmount() {
        return amount;
    }

    @Override
    public String cardToString(){
        StringBuilder toPrint;
        toPrint = new StringBuilder("Lorenzo gains " + amount + " faith");
        if(isRefresh())
            toPrint.append(" and shuffles his deck");

        toPrint.append("\n");

        return toPrint.toString();
    }
}
