package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.PlayerAbilityDiscountsMessage;
import it.polimi.ingsw.utils.messages.server.PlayerAbilityMarblesMessage;
import it.polimi.ingsw.utils.messages.server.PlayerDevelopmentBSpacesMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class AbilityMarblesChangeListener extends ModelChangeListener {

    public AbilityMarblesChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerAbilityMarblesMessage((List<ResourceType>) newValue.getProperty(), newValue.getNickname()));
    }
}
