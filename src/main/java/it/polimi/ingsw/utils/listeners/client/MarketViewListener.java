package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.MarketBoardController;
import it.polimi.ingsw.client.structures.MarketView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Listener for the market actions in a MarketView
 */
public class MarketViewListener implements PropertyChangeListener {

    private final MarketBoardController marketBoardController;

    /**
     * Constructor for the listener
     * @param marketBoardController Associated GUI Controller
     */
    public MarketViewListener(MarketBoardController marketBoardController) {
        this.marketBoardController = marketBoardController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        marketBoardController.showMarket((MarketView) evt.getNewValue());
    }
}
