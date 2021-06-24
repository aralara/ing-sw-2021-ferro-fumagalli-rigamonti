package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.FaithTrackView;
import it.polimi.ingsw.server.model.faith.*;

/**
 * Server setup message for the game's faith track
 */
public class FaithTrackMessage implements ServerUpdateMessage {

    private final FaithTrackView faithTrack;


    /**
     * Constructor for a FaithTrackMessage given a faith track
     * @param faithTrack FaithTrack contained in the FaithTrackMessage
     */
    public FaithTrackMessage(FaithTrack faithTrack) {
        this.faithTrack = new FaithTrackView(faithTrack);
    }


    @Override
    public void doUpdate(ClientController client) {
        client.setFaithTrack(faithTrack);
    }
}
