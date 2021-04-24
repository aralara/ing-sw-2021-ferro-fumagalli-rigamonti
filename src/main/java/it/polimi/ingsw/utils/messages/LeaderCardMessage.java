package it.polimi.ingsw.utils.messages;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public class LeaderCardMessage implements Message {

    private List<LeaderCard> leaderCards;


    public LeaderCardMessage(List<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }


    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }
}
