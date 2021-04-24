package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.LeaderCardMessage;

import java.util.List;

public class LeaderCardDiscardMessage extends LeaderCardMessage {

    public LeaderCardDiscardMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
    }
}
