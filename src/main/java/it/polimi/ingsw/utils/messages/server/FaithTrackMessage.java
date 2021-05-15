package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.FaithTrackView;
import it.polimi.ingsw.client.structures.VaticanReportView;
import it.polimi.ingsw.server.model.faith.*;

import java.util.ArrayList;
import java.util.List;

public class FaithTrackMessage implements ServerUpdateMessage {

    private final List<VaticanReport> vaticanReports;
    private final List<FaithSpace> faithSpaces;


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

    @Override
    public void doUpdate(ClientController client) {
        client.getFaithTrack().setVaticanReports(vaticanReports);
        client.getFaithTrack().setFaithSpaces(faithSpaces);
    }
}
