package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
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
    public void doUpdate(ClientController client) {  //TODO: considerare la conversione da VaticanReport a VaticanReportView nel costruttore del messaggio o nel set del FaithTrackView
        List<VaticanReportView> clientVaticanReports = new ArrayList<>();
        for(VaticanReport vaticanReport : vaticanReports) {
            clientVaticanReports.add(new VaticanReportView(vaticanReport.getMin(),
                    vaticanReport.getMax(), vaticanReport.getPopeValue()));
        }
        client.getFaithTrackView().setFaithTrackView(clientVaticanReports, faithSpaces);
    }
}
