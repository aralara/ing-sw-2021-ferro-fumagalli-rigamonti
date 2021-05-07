package it.polimi.ingsw.client.structures;

public class FaithSpaceView {

    private final int VP;
    private final int position;


    public FaithSpaceView(int VP, int position) {
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
