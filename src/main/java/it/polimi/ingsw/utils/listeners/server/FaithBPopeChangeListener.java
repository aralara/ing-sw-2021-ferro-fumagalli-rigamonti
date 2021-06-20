package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerFaithBPopeMessage;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the faith board's pope spaces in a PlayerBoard
 */
public class FaithBPopeChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public FaithBPopeChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerFaithBPopeMessage((boolean[]) newValue.getProperty(), newValue.getNickname()));
    }
}
