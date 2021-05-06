package it.polimi.ingsw.client.structures;

public class VaticanReportView {

    private final int min, max;
    private final int popeValue;


    public VaticanReportView(int min, int max, int popeValue) {
        this.min = min;
        this.max = max;
        this.popeValue = popeValue;
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
