package it.polimi.ingsw.utils.listeners.server;

import it.polimi.ingsw.server.model.cards.deck.Deck;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.update.PlayerLeaderBHandMessage;

import java.beans.PropertyChangeEvent;

/**
 * Listener for the leader board's hand in a PlayerBoard
 */
public class LeaderBHandChangeListener extends ModelChangeListener {

    /**
     * Constructor for the listener
     * @param virtualView Associated VirtualView
     */
    public LeaderBHandChangeListener(VirtualView virtualView) {
        super(virtualView);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Deck newDeck = (Deck) ((PlayerProperty) evt.getNewValue()).getProperty();
        String nickname = ((PlayerProperty) evt.getNewValue()).getNickname();
        PlayerLeaderBHandMessage message = new PlayerLeaderBHandMessage(newDeck, nickname);

        // The cards are hidden to the player if the message isn't being sent to the owner of the LeaderCard hand
        if(!nickname.equals(getVirtualView().getNickname()))
            message.hide();

        getVirtualView().sendMessage(message);
    }
}
