package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ServerActionAckMessage;
import it.polimi.ingsw.utils.messages.server.ack.CanBuyDevelopmentCardAckMessage;

import java.util.List;

public class CanBuyDevelopmentCardMessage extends ClientActionMessage {

    private final DevelopmentCard developmentCard;
    private final int space;


    public CanBuyDevelopmentCardMessage(DevelopmentCard developmentCard, int space) {
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
        view.sendMessage(new ServerActionAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        List<RequestResources> requestResources = client.chooseStorages(developmentCard.getCost());
        client.getMessageHandler().sendMessage(
                new RequestResourcesDevMessage(developmentCard, space, requestResources));
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.setMainActionPlayed(false);
        client.selectDevDecks();
    }
}
