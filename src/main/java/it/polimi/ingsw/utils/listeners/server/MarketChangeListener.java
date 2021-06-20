package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.MarketMessage;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the market in a Game
 */
public class MarketChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public MarketChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new MarketMessage((Market) evt.getNewValue()));
    }
}
