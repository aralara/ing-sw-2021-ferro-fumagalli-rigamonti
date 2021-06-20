package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.view.VirtualView;

import java.beans.PropertyChangeListener;

/**
 * Generic listener for a model attribute
 */
public abstract class ModelChangeListener implements PropertyChangeListener {

    private final VirtualView virtualView;

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public ModelChangeListener(VirtualView virtualView) {
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
