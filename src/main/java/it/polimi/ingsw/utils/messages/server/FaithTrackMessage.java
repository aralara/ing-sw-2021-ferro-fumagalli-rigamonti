package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.faith.*;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class FaithTrackMessage implements Message {

    private List<VaticanReport> vaticanReports;
    private List<FaithSpace> faithSpaces;


    public FaithTrackMessage(FaithTrack faithTrack) {
        this.vaticanReports = faithTrack.getVaticanReports();
        this.faithSpaces = faithTrack.getFaithSpaces();
    }


    public List<VaticanReport> getVaticanReports() {
        return vaticanReports;
    }

    public List<FaithSpace> getFaithSpaces() {
        return faithSpaces;
    }
}
