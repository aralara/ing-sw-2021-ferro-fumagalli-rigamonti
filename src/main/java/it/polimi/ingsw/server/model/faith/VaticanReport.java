package it.polimi.ingsw.server.model.faith;

import java.io.Serializable;

/**
 * Handles methods relative to the vatican report
 */
public class VaticanReport implements Serializable {

    private final int min, max;
    private final int popeValue;
    private boolean triggered;


    /**
     * VaticanReport constructor with parameters
     * @param min Lower limit for the VaticanReport
     * @param max Upper limit for the VaticanReport
     * @param popeValue Value of the pope card on the VaticanReport
     */
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
    public boolean inRange(int position) {
        return position >= min;
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

    /**
     * Gets the popeValue attribute
     * @return Returns popeValue
     */
    public int getPopeValue() {
        return this.popeValue;
    }

    /**
     * Gets the triggered attribute
     * @return Returns triggered value
     */
    public boolean getTriggered() {
        return this.triggered;
    }

    /**
     * Sets the triggered attribute
     * @param triggered New attribute value
     */
    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }
}
