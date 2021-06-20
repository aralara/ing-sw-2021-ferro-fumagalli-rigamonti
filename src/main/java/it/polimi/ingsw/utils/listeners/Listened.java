package it.polimi.ingsw.utils.listeners;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Generic listened class for a model object
 */
public abstract class Listened {

    private final PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private int listenerNumber = 0;

    /**
     * Adds a listener to the current listened object
     * @param name Name of the listener
     * @param l Listener object
     */
    public void addListener(String name, PropertyChangeListener l) {
        listeners.addPropertyChangeListener(name, l);
        listenerNumber++;
    }

    /**
     * Removes a listener from the current listened object
     * @param name Name of the listener
     * @param l Listener object
     */
    public void removeListener(String name, PropertyChangeListener l) {
        listeners.removePropertyChangeListener(name, l);
        listenerNumber--;
    }

    /**
     * Fires an update to the current listeners of the listened object, the old value parameter is not used
     * and therefore set null by default
     * @param propertyName Name of the changed property
     * @param newValue New value of the property
     */
    public void fireUpdate(String propertyName, Object newValue){
        listeners.firePropertyChange(propertyName, null, newValue);
    }

    /**
     * Checks if the listened object has any listeners attached to it
     * @return Returns true if the listened object has at least one listener, false otherwise
     */
    public boolean hasListeners() {
        return listenerNumber > 0;
    }
}
