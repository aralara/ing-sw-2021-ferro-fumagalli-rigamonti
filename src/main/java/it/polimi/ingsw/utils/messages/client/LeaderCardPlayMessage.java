package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.LeaderCardMessageClient;
import it.polimi.ingsw.utils.messages.server.ServerActionAckMessage;

import java.util.List;

public class LeaderCardPlayMessage extends LeaderCardMessageClient {

    public LeaderCardPlayMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
    }


    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.playLeaderCard(view.getNickname(), getLeaderCards());
        view.sendMessage(new ServerActionAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: deve fare qualcosa qui?
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        //TODO: deve fare qualcosa qui?
    }
}
