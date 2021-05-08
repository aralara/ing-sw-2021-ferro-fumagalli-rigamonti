package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.LeaderCardMessage;
import it.polimi.ingsw.utils.messages.server.ack.LeaderCardPlayAckMessage;

import java.util.List;

public class LeaderCardPlayMessage extends LeaderCardMessage {

    public LeaderCardPlayMessage(LeaderCard leaderCard) {
        super(leaderCard);
    }


    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.playLeaderCard(view.getNickname(), getLeaderCard());
        view.sendMessage(new LeaderCardPlayAckMessage(success));
    }
}
