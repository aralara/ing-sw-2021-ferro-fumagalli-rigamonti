package it.polimi.ingsw.server.model.faith;

import java.io.Serializable;

public class FaithSpace implements Serializable {

    private final int VP;
    private final int position;


    /**
     * FaithSpace constructor with parameters
     * @param VP Victory points granted by the FaithSpace
     * @param position Position of the FaithSpace inside the FaithTrack
     */
    public FaithSpace(int VP, int position) {
        this.VP = VP;
        this.position = position;
    }


    /**
     * Gets the VP amount
     * @return Returns VP
     */
    public int getVP() {
        return this.VP;
    }

    /**
     * Gets the position attribute
     * @return Returns position
     */
    public int getPosition() {
        return this.position;
    }
}
