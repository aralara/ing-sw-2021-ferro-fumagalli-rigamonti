package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.boards.LorenzoBoard;

/**
 * Handles methods relative to the Lorenzo faith card
 */
public class LorenzoFaith extends LorenzoCard {

    private final boolean refresh;
    private final int amount;


    /**
     * Constructor for a LorenzoFaith card
     * @param ID Unique ID reference for the card
     * @param refresh Value that indicates if the card should refresh the Lorenzo card pile
     * @param amount Amount of faith that the card grants to Lorenzo
     */
    public LorenzoFaith(int ID, boolean refresh, int amount) {
        setID(ID);
        this.refresh = refresh;
        this.amount = amount;
    }

    /**
     * Copy constructor for a LorenzoFaith card
     * @param lorenzoFaith LorenzoFaith to copy
     */
    public LorenzoFaith(LorenzoFaith lorenzoFaith) {
        setID(lorenzoFaith.getID());
        this.refresh = lorenzoFaith.refresh;
        this.amount = lorenzoFaith.amount;
    }


    @Override
    public String toString(){
        StringBuilder toPrint;
        toPrint = new StringBuilder("Lorenzo gains " + amount + " faith");
        if(isRefresh())
            toPrint.append(" and shuffles his deck");
        toPrint.append("\n");
        return toPrint.toString();
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
}
