package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.faith.FaithSpace;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackView {

    private final List<VaticanReportView> vaticanReports;
    private final List<FaithSpace> faithSpaces;


    public FaithTrackView() {
        this.vaticanReports = new ArrayList<>();
        this.faithSpaces = new ArrayList<>();
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
