package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
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
        boolean success = view.getGameHandler().getController()
                .discardLeaders(view.getNickname(), getLeaderCards(), setup);
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: gestione ACK
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        //TODO: gestione NACK
    }
}
