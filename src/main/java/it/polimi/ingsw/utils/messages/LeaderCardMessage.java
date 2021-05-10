package it.polimi.ingsw.utils.messages;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public abstract class LeaderCardMessage implements ActionMessage {

    private final List<LeaderCard> leaderCards;


    public LeaderCardMessage(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }


    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
