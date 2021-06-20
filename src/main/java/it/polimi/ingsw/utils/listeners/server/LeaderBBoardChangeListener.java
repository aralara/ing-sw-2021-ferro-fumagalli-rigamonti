package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerLeaderBBoardMessage;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the leader board's board in a PlayerBoard
 */
public class LeaderBBoardChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
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
