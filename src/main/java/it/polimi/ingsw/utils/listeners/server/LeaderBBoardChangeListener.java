package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerLeaderBBoardMessage;

import java.beans.PropertyChangeEvent;

/**
 * TODO: fare javadoc
 */
public class LeaderBBoardChangeListener extends ModelChangeListener {

    public LeaderBBoardChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        PlayerProperty newValue = (PlayerProperty) evt.getNewValue();
        getVirtualView().sendMessage(
                new PlayerLeaderBBoardMessage((Deck) newValue.getProperty(), newValue.getNickname()));
    }
}
