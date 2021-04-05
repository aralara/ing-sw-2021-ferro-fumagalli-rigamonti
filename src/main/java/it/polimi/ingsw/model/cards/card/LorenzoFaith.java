package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public class LorenzoFaith implements LorenzoCard{

    private boolean refresh;
    private int amount;


    LorenzoFaith() {

    }

    LorenzoFaith(LorenzoFaith card) {
        this.refresh = card.isRefresh();
        this.amount = card.getAmount();
    }


    @Override
    public LorenzoFaith makeClone() {
        return new LorenzoFaith(this);
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
