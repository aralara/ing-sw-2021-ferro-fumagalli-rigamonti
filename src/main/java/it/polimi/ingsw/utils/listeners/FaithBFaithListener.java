package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerFaithBFaithMessage;

import java.beans.PropertyChangeEvent;

public class FaithBFaithListener extends ModelChangeListener {

    public FaithBFaithListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerFaithBFaithMessage((int) newValue.getProperty(), newValue.getNickname()));
    }
}
