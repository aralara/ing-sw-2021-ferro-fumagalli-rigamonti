package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;

import java.util.List;

/**
 * TODO: fare javadoc
 */
public abstract class LeaderCardMessageClient extends ClientActionMessage {

    private final List<LeaderCard> leaderCards;


    public LeaderCardMessageClient(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }


    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
