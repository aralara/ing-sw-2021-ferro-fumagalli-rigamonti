package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.LeaderCardMessageClient;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.List;

public class LeaderCardDiscardMessage extends LeaderCardMessageClient {

    private final boolean setup;


    public LeaderCardDiscardMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
        this.setup = false;
    }
    public LeaderCardDiscardMessage(List<LeaderCard> leaderCards, boolean setup) {
        super(leaderCards);
        this.setup = setup;
    }


    @Override
    public void doAction(VirtualView view) {
        view.getGameHandler().getController().discardLeaders(view.getNickname(), getLeaderCards(), setup);
        view.sendMessage(new ServerAckMessage(getUuid(), true));
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
