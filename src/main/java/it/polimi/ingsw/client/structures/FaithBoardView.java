package it.polimi.ingsw.client.structures;

public class FaithBoardView {

    public static final int POPE_PROGRESSION_SIZE = 3; //TODO: serve?

    private int faith;
    private boolean[] popeProgression;


    public FaithBoardView() {
        this.faith = 0;
        this.popeProgression = new boolean[POPE_PROGRESSION_SIZE];
    }

    public FaithBoardView(int faith, boolean[] popeProgression) {
        this.faith = faith;
        this.popeProgression = popeProgression;
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

    /**
     * Gets the popeProgression attribute
     * @return Returns popeProgression
     */
    public boolean[] getPopeProgression() {
        return popeProgression;
    }

    /**
     * Sets the popeProgression attribute
     * @param popeProgression New attribute value
     */
    public void setPopeProgression(boolean[] popeProgression) {
        this.popeProgression = popeProgression;
    }
}
