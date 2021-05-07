package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.faith.FaithSpace;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackView {

    private List<VaticanReportView> vaticanReports;
    private List<FaithSpace> faithSpaces;


    public FaithTrackView() {
        this.vaticanReports = new ArrayList<>();
        this.faithSpaces = new ArrayList<>();
    }


    public void setFaithTrackView(List<VaticanReportView> vaticanReports, List<FaithSpace> faithSpaces) {
        this.vaticanReports = vaticanReports;
        this.faithSpaces = faithSpaces;
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
