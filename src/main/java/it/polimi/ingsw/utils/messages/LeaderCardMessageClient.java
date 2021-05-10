package it.polimi.ingsw.utils.messages;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.client.ClientActionMessage;

import java.util.List;

public abstract class LeaderCardMessageClient implements ClientActionMessage {

    private final List<LeaderCard> leaderCards;


    public LeaderCardMessageClient(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }


    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
