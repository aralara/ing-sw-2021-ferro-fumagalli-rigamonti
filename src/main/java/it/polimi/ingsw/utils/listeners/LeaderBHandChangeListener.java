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
        Deck newDeck = (Deck) ((PlayerProperty) evt.getNewValue()).getProperty();
        String nickname = ((PlayerProperty) evt.getNewValue()).getNickname();
        PlayerLeaderBHandMessage message = new PlayerLeaderBHandMessage(newDeck, nickname);

        // The cards are hidden to the player if the message isn't being sent to the owner of the LeaderCard hand
        if(!nickname.equals(getVirtualView().getNickname()))
            message.hide();

        getVirtualView().sendMessage(message);
    }
}
