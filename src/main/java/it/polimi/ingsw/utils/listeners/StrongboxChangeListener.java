package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.PlayerStrongBoxMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class StrongboxChangeListener extends ModelChangeListener {

    public StrongboxChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendUpdateMessage(
                new PlayerStrongBoxMessage((List<Resource>) newValue.getProperty(), newValue.getNickname()));
    }
}
