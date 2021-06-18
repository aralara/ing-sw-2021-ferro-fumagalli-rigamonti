package it.polimi.ingsw.utils.listeners.client;

import it.polimi.ingsw.client.gui.controllers.MarketBoardController;
import it.polimi.ingsw.client.structures.MarketView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * TODO: fare javadoc
 */
public class MarketViewListener implements PropertyChangeListener {

    private final MarketBoardController marketBoardController;

    public MarketViewListener(MarketBoardController marketBoardController) {
        this.marketBoardController = marketBoardController;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        marketBoardController.showMarket((MarketView) evt.getNewValue());
    }
}
