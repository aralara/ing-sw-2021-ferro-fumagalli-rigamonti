package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.view.VirtualView;

import java.beans.PropertyChangeEvent;

public class WarehouseChangeListener extends ViewChangeListener {

    public WarehouseChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
