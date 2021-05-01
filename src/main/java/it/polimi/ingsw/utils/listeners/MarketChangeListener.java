package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.view.VirtualView;

import java.beans.PropertyChangeEvent;

public class MarketChangeListener extends ViewChangeListener {

    public MarketChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
