package it.polimi.ingsw.client.structures;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackView {

    private final List<VaticanReportView> vaticanReports;
    private final List<FaithSpaceView> faithSpaces;


    public FaithTrackView() {
        vaticanReports = new ArrayList<>();
        faithSpaces = new ArrayList<>();
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
    public List<FaithSpaceView> getFaithSpaces() {
        return faithSpaces;
    }
}
