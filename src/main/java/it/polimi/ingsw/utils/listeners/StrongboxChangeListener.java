package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.view.VirtualView;

import java.beans.PropertyChangeEvent;

public class StrongboxChangeListener extends ViewChangeListener {

    public StrongboxChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
