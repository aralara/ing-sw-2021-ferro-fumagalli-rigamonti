package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.PlayerDevelopmentBSpacesMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class DevelopmentBSpacesChangeListener extends ModelChangeListener {

    public DevelopmentBSpacesChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendUpdateMessage(
                new PlayerDevelopmentBSpacesMessage((List<Deck>) newValue.getProperty(), newValue.getNickname()));
    }
}
