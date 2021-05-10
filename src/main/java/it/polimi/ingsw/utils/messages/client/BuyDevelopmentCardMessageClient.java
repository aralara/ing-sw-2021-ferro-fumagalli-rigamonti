package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.BuyDevelopmentCardAckMessage;

public class BuyDevelopmentCardMessageClient implements ClientActionMessage {

    private final DevelopmentCard developmentCard;
    private final int space;


    public BuyDevelopmentCardMessageClient(DevelopmentCard developmentCard, int space) {
        this.developmentCard = developmentCard;
        this.space = space;
    }


    public DevelopmentCard getDevelopmentCard() {
        return developmentCard;
    }

    public int getSpace() {
        return space;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.canBuyDevCard(view.getNickname(), developmentCard);
        view.sendMessage(new BuyDevelopmentCardAckMessage(success));
    }
}
