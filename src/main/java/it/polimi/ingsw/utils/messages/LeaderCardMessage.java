package it.polimi.ingsw.utils.messages;

import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.utils.messages.Message;

import java.util.List;

public abstract class LeaderCardMessage implements ActionMessage {

    private final LeaderCard leaderCard;


    public LeaderCardMessage(LeaderCard leaderCard) {
        this.leaderCard = leaderCard;
    }


    public LeaderCard getLeaderCard() {
        return leaderCard;
    }
}
