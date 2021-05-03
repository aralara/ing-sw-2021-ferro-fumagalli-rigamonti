package it.polimi.ingsw.utils.listeners;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.PlayerLeaderBBoardMessage;

import java.beans.PropertyChangeEvent;

public class LeaderBBoardChangeListener extends ModelChangeListener {

    public LeaderBBoardChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendUpdateMessage(
                new PlayerLeaderBBoardMessage((Deck) newValue.getProperty(), newValue.getNickname()));
    }
}
