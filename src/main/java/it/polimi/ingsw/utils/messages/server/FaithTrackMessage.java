package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.model.faith.*;

import java.util.List;

public class FaithTrackMessage implements ServerActionMessage {

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
    public void doAction(CLI client) {
        client.faithTrackSetup(this);
    }
}
