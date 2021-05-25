package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerFaithBPopeMessage;

import java.beans.PropertyChangeEvent;

public class FaithBPopeChangeListener extends ModelChangeListener {

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
