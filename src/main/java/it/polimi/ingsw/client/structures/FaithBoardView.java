package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.boards.FaithBoard;

import java.io.Serializable;

import static it.polimi.ingsw.utils.Constants.FAITH_TOTAL_VATICAN_REPORTS;

public class FaithBoardView implements Serializable {

    private int faith;
    private boolean[] popeProgression;


    public FaithBoardView() {
        this.faith = 0;
        this.popeProgression = new boolean[FAITH_TOTAL_VATICAN_REPORTS.value()];
    }

    public FaithBoardView(FaithBoard faithBoard) {
        this.faith = faithBoard.getFaith();
        this.popeProgression = faithBoard.getPopeProgression();
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
