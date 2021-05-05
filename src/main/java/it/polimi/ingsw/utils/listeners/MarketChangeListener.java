package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.market.Market;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.MarketMessage;

import java.beans.PropertyChangeEvent;

public class MarketChangeListener extends ModelChangeListener {

    public MarketChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new MarketMessage((Market) evt.getNewValue()));
    }
}
