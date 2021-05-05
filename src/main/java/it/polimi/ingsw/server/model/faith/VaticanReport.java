package it.polimi.ingsw.server.model.faith;

import java.io.Serializable;

public class VaticanReport implements Serializable {

    private final int min, max;
    private final int popeValue;
    private boolean triggered;


    public VaticanReport(int min, int max, int popeValue) {
        this.min = min;
        this.max = max;
        this.popeValue = popeValue;
        triggered = false;
    }


    /**
     * Checks if a position is in the range specified by the vatican report
     * @param position Player position
     * @return Returns true if it is in range, false otherwise
     */
    public boolean inRange(int position) { //!!
        return position >= min;
    }

    /**
     * Gets the popeValue attribute
     * @return Returns popeValue
     */
    public int getPopeValue() {
        return this.popeValue;
    }

    /**
     * Sets the triggered attribute
     * @param triggered New attribute value
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    /**
     * Gets the triggered attribute
     * @return Returns triggered value
     */
    public boolean getTriggered() {
        return this.triggered;
    }

    /**
     * Gets the max attribute
     * @return Returns max value
     */
    public int getMax() {
        return this.max;
    }

    /**
     * Gets the min attribute
     * @return Returns min value
     */
    public int getMin() {
        return this.min;
    }
}
