package it.polimi.ingsw.model.faith;

public class VaticanReport {

    private int min, max;
    private int popeValue;
    private boolean triggered;


    public VaticanReport(int min, int max, int popeValue){

    }


    /**
     * Checks if a position is in the range specified by the vatican report
     * @param position Player position
     * @return Returns true if it is in range, false otherwise
     */
    public boolean inRange(int position) {
        return false;
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
}
