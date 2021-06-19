package it.polimi.ingsw.client.structures;

import it.polimi.ingsw.server.model.market.Marble;
import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.utils.listeners.Listened;
import it.polimi.ingsw.utils.listeners.Listeners;

import java.io.Serializable;

/**
 * Handles market client methods
 */
public class MarketView extends Listened implements Serializable {

    private Marble[][] marbleMatrix;
    private Marble floatingMarble;


    /**
     * MarketView constructor with parameters
     * @param market Market to set
     */
    public MarketView(Market market) {
        this.marbleMatrix = market.getMarbleMatrix();
        this.floatingMarble = market.getFloatingMarble();
    }


    /**
     * Updates the current MarketView object referencing a Market object passed as a parameter
     * @param market Market object to copy
     */
    public void setMarket(MarketView market) {
        this.marbleMatrix = market.getMarbleMatrix();
        this.floatingMarble = market.getFloatingMarble();
        if(hasListeners())
            fireUpdate(Listeners.GAME_MARKET.value(), this);
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
