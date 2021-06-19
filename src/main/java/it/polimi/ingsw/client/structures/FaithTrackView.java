package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.faith.FaithSpace;
import it.polimi.ingsw.server.model.faith.FaithTrack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles faithTrack client methods
 */
public class FaithTrackView implements Serializable {

    private final List<VaticanReportView> vaticanReports;
    private final List<FaithSpace> faithSpaces;


    /**
     * FaithTrackView constructor with parameters
     * @param faithTrack FaithTrack to set
     */
    public FaithTrackView(FaithTrack faithTrack) {
        this.vaticanReports = new ArrayList<>();
        faithTrack.getVaticanReports().forEach(vr -> vaticanReports.add(new VaticanReportView(vr)));
        this.faithSpaces = faithTrack.getFaithSpaces();
    }

    /**
     * Gets the vaticanReports attribute
     * @return Returns vaticanReports
     */
    public List<VaticanReportView> getVaticanReports() {
        return vaticanReports;
    }

    /**
     * Gets the faithSpaces attribute
     * @return Returns faithSpaces
     */
    public List<FaithSpace> getFaithSpaces() {
        return faithSpaces;
    }
}
