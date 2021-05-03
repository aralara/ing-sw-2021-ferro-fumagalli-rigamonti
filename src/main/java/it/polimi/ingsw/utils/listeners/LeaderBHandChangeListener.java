package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.PlayerLeaderBHandMessage;

import java.beans.PropertyChangeEvent;

public class LeaderBHandChangeListener extends ModelChangeListener {

    public LeaderBHandChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendUpdateMessage(
                new PlayerLeaderBHandMessage((Deck) newValue.getProperty(), newValue.getNickname()));
    }
}
