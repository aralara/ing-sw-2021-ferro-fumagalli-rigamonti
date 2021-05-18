package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.LeaderCardMessageClient;
import it.polimi.ingsw.utils.messages.server.ack.LeaderCardDiscardAckMessage;

import java.util.List;

public class LeaderCardDiscardMessage extends LeaderCardMessageClient {

    public LeaderCardDiscardMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
    }


    @Override
    public void doAction(VirtualView view, Controller controller) {
        controller.discardLeaders(view.getNickname(), getLeaderCards());
        view.sendMessage(new LeaderCardDiscardAckMessage(true));
    }

    @Override
    public void doACKResponseAction(ClientController client) {

    }

    @Override
    public void doNACKResponseAction(ClientController client) {

    }
}
