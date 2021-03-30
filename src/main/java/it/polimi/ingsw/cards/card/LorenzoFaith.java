package it.polimi.ingsw.cards.card;

public class LorenzoFaith implements LorenzoCard{

    private boolean refresh;
    private int amount;


    LorenzoFaith(){

    }


    /**
     * Gets the refresh attribute
     * @return Returns refresh value
     */
    public boolean getRefresh(){
        return refresh;
    }

    /**
     * Gets the amount attribute
     * @return Returns amount value
     */
    public int getAmount(){
        return amount;
    }
}
