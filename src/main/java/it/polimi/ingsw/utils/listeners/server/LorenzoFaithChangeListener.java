package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.LorenzoFaithMessage;

import java.beans.PropertyChangeEvent;

/**
 * Listener for Lorenzo's faith
 */
public class LorenzoFaithChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public LorenzoFaithChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new LorenzoFaithMessage((int) evt.getNewValue()));
    }
}
