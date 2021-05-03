package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.DevelopmentDecksMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class DevelopmentDeckChangeListener extends ModelChangeListener {

    public DevelopmentDeckChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendUpdateMessage(new DevelopmentDecksMessage(List.of((DevelopmentDeck) evt.getNewValue())));
    }
}
