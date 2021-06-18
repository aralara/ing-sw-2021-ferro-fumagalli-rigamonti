package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.faith.VaticanReport;

import java.io.Serializable;

/**
 * Handles VaticanReport client methods
 */
public class VaticanReportView implements Serializable {

    private final int min, max;
    private final int popeValue;

    /**
     * Constructor with parameter
     * @param vaticanReport VaticanReport which parameters are to be set
     */
    public VaticanReportView(VaticanReport vaticanReport) {
        this.min = vaticanReport.getMin();
        this.max = vaticanReport.getMax();
        this.popeValue = vaticanReport.getPopeValue();
    }


    /**
     * Gets the min attribute
     * @return Returns min
     */
    public int getMin() {
        return min;
    }

    /**
     * Gets the max attribute
     * @return Returns max
     */
    public int getMax() {
        return max;
    }

    /**
     * Gets the popeValue attribute
     * @return Returns popeValue
     */
    public int getPopeValue() {
        return popeValue;
    }
}
