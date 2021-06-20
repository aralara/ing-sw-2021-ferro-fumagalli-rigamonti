package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerAbilityDiscountsMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Listener for the ability discounts in a PlayerBoard
 */
public class AbilityDiscountsChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public AbilityDiscountsChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerAbilityDiscountsMessage((List<ResourceType>) newValue.getProperty(), newValue.getNickname()));
    }
}
