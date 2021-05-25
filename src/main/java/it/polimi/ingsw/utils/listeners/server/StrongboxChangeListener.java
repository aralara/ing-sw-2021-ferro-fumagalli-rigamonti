package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerStrongBoxMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class StrongboxChangeListener extends ModelChangeListener {

    public StrongboxChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerStrongBoxMessage((List<Resource>) newValue.getProperty(), newValue.getNickname()));
    }
}
