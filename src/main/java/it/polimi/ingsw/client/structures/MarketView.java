package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.market.Marble;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;


public class MarketView {

    private Marble[][] marbleMatrix;
    private Marble floatingMarble;


    public MarketView(){
        this.marbleMatrix = new Marble[MARKET_ROW_SIZE.value()][MARKET_COLUMN_SIZE.value()];
    }


    /**
     * Gets the marbleMatrix attribute
     * @return Returns a copy of marbleMatrix
     */
    public Marble[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    /**
     * Sets the marbleMatrix attribute
     * @param marbleMatrix New attribute value
     */
    public void setMarbleMatrix(Marble[][] marbleMatrix) {
        this.marbleMatrix = marbleMatrix;
    }

    /**
     * Gets the floatingMarble attribute
     * @return Returns floatingMarble
     */
    public Marble getFloatingMarble() {
        return floatingMarble;
    }

    /**
     * Sets the floatingMarble attribute
     * @param floatingMarble New attribute value
     */
    public void setFloatingMarble(Marble floatingMarble) {
        this.floatingMarble = floatingMarble;
    }
}
