package it.polimi.ingsw.model.faith;

import java.util.List;

public class FaithTrack {

    private List<VaticanReport> vaticanReports;
    private List<FaithSpace> faithSpaces;
    private int lastReportTriggered = -1;


    public FaithTrack(){

    }


    /**
     * Loads faithSpaces and vaticanReports from the file given by parameter
     * @param fileName Path of the file that contains the information
     */
    public void loadTrack(String fileName){

    }

    /**
     * Checks if a player has landed or surpassed a Pope space, triggering a vatican report,
     * if the vatican report is triggered it is also updated
     * @param position Position of the player in the faith track
     * @return Returns true if a vatican report is triggered, false otherwise
     */
    public boolean checkReportActivation(int position){
        return false;
    }

    /**
     * Method invoked on a player during a vatican report to check if the current player is in the last triggered
     * report range in order to turn their favor tiles
     * @param position Position of the player in the faith track
     * @return Returns true if the player is in range, false otherwise
     */
    public boolean checkPlayerReportPosition(int position) {
        return false;
    }

    /**
     * Calculates total VPs given by the faith track for a player calling calculateFaithSpaceVP and calculateReportVP
     * @param position Position of the player in the faith track
     * @param popeProgression Array containing Pope's favor tiles states of the player
     * @return Returns total VP amount
     */
    public int calculateVP(int position, boolean[] popeProgression) {
        return -1;
    }

    /**
     * Calculates VPs for the last faith space visited by a player
     * @param position Position of the player in the faith track
     * @return Returns VP amount
     */
    private int calculateFaithSpaceVP(int position) {
        return -1;
    }

    /**
     * Calculates VPs given by the active Pope's favor tiles of a player
     * @param popeProgression Array containing Pope's favor tiles states of the player
     * @return Returns VP amount
     */
    private int calculateReportVP(boolean[] popeProgression) {
        return -1;
    }
}
