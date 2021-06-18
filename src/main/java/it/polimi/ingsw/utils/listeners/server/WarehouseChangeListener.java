package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.storage.Shelf;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerWarehouseMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * TODO: fare javadoc
 */
public class WarehouseChangeListener extends ModelChangeListener {

    public WarehouseChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerWarehouseMessage((List<Shelf>) newValue.getProperty(), newValue.getNickname()));
    }
}
