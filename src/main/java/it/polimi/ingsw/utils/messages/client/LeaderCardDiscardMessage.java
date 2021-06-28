package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.LeaderCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.List;

/**
 * Client message that discards leader cards from the player's hand
 */
public class LeaderCardDiscardMessage extends LeaderCardMessageClient {

    private final boolean setup;


    /**
     * Constructor for a LeaderCardDiscardMessage given the discarded LeaderCards
     * @param leaderCards LeaderCards that need to be discarded
     */
    public LeaderCardDiscardMessage(List<LeaderCard> leaderCards) {
        super(leaderCards);
        this.setup = false;
    }

    /**
     * Constructor for a LeaderCardDiscardMessage given the discarded LeaderCards and a setup flag
     * @param leaderCards LeaderCards that need to be discarded
     * @param setup Flag that indicates if the cards are discarded during setup
     */
    public LeaderCardDiscardMessage(List<LeaderCard> leaderCards, boolean setup) {
        super(leaderCards);
        this.setup = setup;
    }


    @Override
    public void doAction(VirtualView view) {
        Controller controller = view.getGameHandler().getController();
        boolean success = false;
        if(controller.checkTurnPlayer(view.getNickname()))
             success = view.getGameHandler().getController()
                     .discardLeaders(view.getNickname(), getLeaderCards(), setup);
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Leaders discarded successfully", false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Unable to discard leaders", true);
    }
}
