package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.DevelopmentDecksMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Listener for the development decks in a Game
 */
public class DevelopmentDeckChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public DevelopmentDeckChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new DevelopmentDecksMessage(List.of((DevelopmentDeck) evt.getNewValue())));
    }
}
