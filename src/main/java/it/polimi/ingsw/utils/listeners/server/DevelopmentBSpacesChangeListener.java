package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerDevelopmentBSpacesMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Listener for the development deck's spaces in a PlayerBoard
 */
public class DevelopmentBSpacesChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public DevelopmentBSpacesChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerDevelopmentBSpacesMessage((List<Deck>) newValue.getProperty(), newValue.getNickname()));
    }
}
