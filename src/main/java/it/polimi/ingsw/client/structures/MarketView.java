package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.Market;

import java.io.Serializable;

import static it.polimi.ingsw.utils.Constants.MARKET_COLUMN_SIZE;
import static it.polimi.ingsw.utils.Constants.MARKET_ROW_SIZE;


public class MarketView implements Serializable {

    private final Marble[][] marbleMatrix;
    private final Marble floatingMarble;


    public MarketView(){
        this.marbleMatrix = new Marble[MARKET_ROW_SIZE.value()][MARKET_COLUMN_SIZE.value()];
        this.floatingMarble = null;
    }

    public MarketView(Market market) {
        this.marbleMatrix = market.getMarbleMatrix();
        this.floatingMarble = market.getFloatingMarble();
    }


    /**
     * Gets the marbleMatrix attribute
     * @return Returns a copy of marbleMatrix
     */
    public Marble[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    /**
     * Gets the floatingMarble attribute
     * @return Returns floatingMarble
     */
    public Marble getFloatingMarble() {
        return floatingMarble;
    }
}
