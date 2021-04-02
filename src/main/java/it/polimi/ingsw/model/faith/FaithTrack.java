package it.polimi.ingsw.model.faith;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FaithTrack {

    private List<VaticanReport> vaticanReports;
    private List<FaithSpace> faithSpaces;
    private int lastReportTriggered;


    public FaithTrack(){
        vaticanReports = new ArrayList<>();
        faithSpaces = new ArrayList<>();
        lastReportTriggered = -1;
    }


    /**
     * Loads faithSpaces and vaticanReports from the file given by parameter
     * @param fileName Path of the file that contains the information
     */
    public void loadTrack(String fileName){
        try {
            File faithTrackFile = new File(fileName);
            Scanner fileReader = new Scanner(faithTrackFile);
            while (fileReader.hasNextLine()) {
                System.out.println("DA CAMBIARE");
                //parsing
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a player has landed or surpassed a Pope space, triggering a vatican report;
     * if the vatican report is triggered, it is also updated, as well as the lastReportTriggered
     * @param position Position of the player in the faith track
     * @return Returns true if a vatican report is triggered, false otherwise
     */
    public boolean checkReportActivation(int position){
        for(int i=vaticanReports.size()-1; i>=0; i--){
            if(position>=vaticanReports.get(i).getMax() && !vaticanReports.get(i).getTriggered()) {
                vaticanReports.get(i).setTriggered(true);
                lastReportTriggered = i;
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the current player is in the last triggered
     * report range in order to turn their favor tiles
     * @param position Position of the player in the faith track
     * @return Returns true if the player is in range, false otherwise
     */
    public boolean checkPlayerReportPosition(int position) {
        return vaticanReports.get(lastReportTriggered).inRange(position);
    }

    /**
     * Calculates total VPs given by the faith track for a player calling calculateFaithSpaceVP and calculateReportVP
     * @param position Position of the player in the faith track
     * @param popeProgression Array containing Pope's favor tiles states of the player
     * @return Returns total VP amount
     */
    public int calculateVP(int position, boolean[] popeProgression) {
        return calculateFaithSpaceVP(position) + calculateReportVP(popeProgression);
    }

    /**
     * Calculates VPs for the last faith space visited by a player
     * @param position Position of the player in the faith track
     * @return Returns VP amount
     */
    private int calculateFaithSpaceVP(int position) {
        for(int i=faithSpaces.size()-1; i>=0; i--){
            if(position>=faithSpaces.get(i).getPosition()) {
                return faithSpaces.get(i).getVP();
            }
        }
        return 0;
    }

    /**
     * Calculates VPs given by the active Pope's favor tiles of a player
     * @param popeProgression Array containing Pope's favor tiles states of the player
     * @return Returns VP amount
     */
    private int calculateReportVP(boolean[] popeProgression) {
        int sumVP = 0;
        for(int i=0; i<vaticanReports.size(); i++)
            if(popeProgression[i])
                sumVP += vaticanReports.get(i).getPopeValue();
        return sumVP;
    }
}
