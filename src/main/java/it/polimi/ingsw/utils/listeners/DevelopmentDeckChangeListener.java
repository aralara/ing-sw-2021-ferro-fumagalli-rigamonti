package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.DevelopmentDecksMessage;

import java.beans.PropertyChangeEvent;
import java.util.List;

public class DevelopmentDeckChangeListener extends ModelChangeListener {

    public DevelopmentDeckChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        getVirtualView().sendMessage(new DevelopmentDecksMessage(List.of((DevelopmentDeck) evt.getNewValue())));
        //TODO: ha generato eccezione per il cast deck-developmentDeck quando compro una devCard
    }
}
