package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerAbilityMarblesMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Listener for the ability marbles in a PlayerBoard
 */
public class AbilityMarblesChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public AbilityMarblesChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerAbilityMarblesMessage((List<ResourceType>) newValue.getProperty(), newValue.getNickname()));
    }
}
