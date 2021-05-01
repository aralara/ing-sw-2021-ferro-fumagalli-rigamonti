package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.view.VirtualView;

import java.beans.PropertyChangeListener;

public abstract class ViewChangeListener implements PropertyChangeListener {

    private final VirtualView virtualView;

    public ViewChangeListener(VirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Gets the virtualView attribute
     * @return Returns virtualView value
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }
}
