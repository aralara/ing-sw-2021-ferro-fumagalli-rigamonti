package it.polimi.ingsw.server.model.faith;

public class FaithSpace {

    private final int VP;
    private final int position;


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
