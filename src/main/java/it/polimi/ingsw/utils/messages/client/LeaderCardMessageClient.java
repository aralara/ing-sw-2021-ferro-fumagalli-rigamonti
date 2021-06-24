package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;

import java.util.List;

/**
 * Generic client action message that contains a list of leaders
 */
public abstract class LeaderCardMessageClient extends ClientActionMessage {

    private final List<LeaderCard> leaderCards;


    /**
     * Constructor for a generic message that contains a list of LeaderCards
     * @param leaderCards List of LeaderCards contained in the message
     */
    public LeaderCardMessageClient(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }


    /**
     * Gets the leaderCards attribute
     * @return Returns leaderCards value
     */
    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
