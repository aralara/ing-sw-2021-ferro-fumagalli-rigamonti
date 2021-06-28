package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.List;

/**
 * Client message that plays leader cards from the player's hand
 */
public class LeaderCardPlayMessage extends LeaderCardMessageClient {

    /**
     * Constructor for a LeaderCardPlayMessage given a list of LeaderCards
     * @param leaderCards List of LeaderCards that need to be played
     */
    public LeaderCardPlayMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
    }


    @Override
    public void doAction(VirtualView view) {
        Controller controller = view.getGameHandler().getController();
        boolean success = false;
        if(controller.checkTurnPlayer(view.getNickname()))
            success = view.getGameHandler().getController().playLeaderCard(view.getNickname(), getLeaderCards());
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Leader played successfully", false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Unable to play leader", true);
    }
}
