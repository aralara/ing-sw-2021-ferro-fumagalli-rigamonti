package it.polimi.ingsw.model.boards;

import it.polimi.ingsw.model.faith.FaithTrack;

public class FaithBoard {
    private static final int POPE_PROGRESSION_SIZE = 3;

    private int faith;
    private boolean[] popeProgression;


    public FaithBoard() {
        faith = 0;
        popeProgression = new boolean[POPE_PROGRESSION_SIZE];
    }


    /**
     * Adds a set amount of faith to the current player
     * @param faith Faith quantity to be added
     */
    public void addFaith(int faith) {
        this.faith += faith;
    }

    /**
     * Gets the faith attribute
     * @return Returns faith
     */
    public int getFaith() {
        return this.faith;
    }

    /**
     * Gets the popeProgression attribute
     * @return Returns popeProgression
     */
    public boolean[] getPopeProgression() {
        return this.popeProgression;
    }

    /**
     * Calculates total VPs given by the faithBoard for a player
     * @param faithTrack FaithTrack utilized to calculate total VPs
     * @return Returns VP amount
     */
    public int calculateVP(FaithTrack faithTrack) {
        return faithTrack.calculateVP(faith, popeProgression);
    }

    /**
     * Handles the activation of a VaticanReport updating popeProgression values
     * @param faithTrack FaithTrack relative to the VaticanReport
     */
    public void handleReportActivation(FaithTrack faithTrack) {
        popeProgression[faithTrack.getLastReportTriggered()] = faithTrack.checkPlayerReportPosition(faith);
    }

}
