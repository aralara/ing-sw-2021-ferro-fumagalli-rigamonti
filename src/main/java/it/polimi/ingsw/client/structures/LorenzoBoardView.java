package it.polimi.ingsw.client.structures;

public class LorenzoBoardView {

    //private Deck lorenzoDeck; //TODO: serve lorenzoDeck?
    private int faith;


    public LorenzoBoardView(/*Deck lorenzoDeck, */int faith) {
        /*for(Card lorenzoCard : lorenzoDeck)
            this.lorenzoDeck.add(lorenzoCard);*/
        this.faith = faith;
    }


    /**
     * Gets the faith attribute
     * @return Returns faith
     */
    public int getFaith() {
        return faith;
    }

    /**
     * Sets the faith attribute
     * @param faith New attribute value
     */
    public void setFaith(int faith) {
        this.faith = faith;
    }
}
