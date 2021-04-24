package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.LeaderCardMessage;

import java.util.List;

public class LeaderCardPlayMessage extends LeaderCardMessage {

    public LeaderCardPlayMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
    }
}
