package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.storage.Production;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.PlayerAbilityProductionsMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class AbilityProductionsChangeListener extends ModelChangeListener {

    public AbilityProductionsChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerAbilityProductionsMessage((List<Production>) newValue.getProperty(), newValue.getNickname()));
    }
}
