package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.faith.FaithSpace;
import it.polimi.ingsw.server.model.faith.VaticanReport;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackView {

    private List<VaticanReportView> vaticanReports;
    private List<FaithSpace> faithSpaces;


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
     *Sets the vaticanReports attribute
     * @param vaticanReports New attribute value
     */
    public void setVaticanReports(List<VaticanReport> vaticanReports) {
        for(VaticanReport vaticanReport : vaticanReports)
            this.vaticanReports.add(new VaticanReportView(vaticanReport.getMin(),
                    vaticanReport.getMax(), vaticanReport.getPopeValue()));
    }

    /**
     * Gets the faithSpaces attribute
     * @return Returns faithSpaces
     */
    public List<FaithSpace> getFaithSpaces() {
        return faithSpaces;
    }

    /**
     *Sets the faithSpaces attribute
     * @param faithSpaces New attribute value
     */
    public void setFaithSpaces(List<FaithSpace> faithSpaces) {
        this.faithSpaces = faithSpaces;
    }
}
