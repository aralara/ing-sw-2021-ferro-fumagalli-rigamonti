package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.LeaderCardMessageClient;
import it.polimi.ingsw.utils.messages.server.ack.LeaderCardPlayAckMessage;

import java.util.List;

public class LeaderCardPlayMessageClient extends LeaderCardMessageClient {

    public LeaderCardPlayMessageClient(List<LeaderCard> leaderCards) {
        super(leaderCards);
    }


    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.playLeaderCard(view.getNickname(), getLeaderCards());
        view.sendMessage(new LeaderCardPlayAckMessage(success));
    }
}
