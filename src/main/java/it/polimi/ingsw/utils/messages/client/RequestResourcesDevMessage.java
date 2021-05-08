package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.RequestResourcesAckMessage;

import java.util.List;

public class RequestResourcesDevMessage extends BuyDevelopmentCardMessage {

    private final List<RequestResources> requestResources;


    public RequestResourcesDevMessage(DevelopmentCard developmentCard, int space, List<RequestResources> requestResources) {
        super(developmentCard, space);
        this.requestResources = requestResources;
    }


    public List<RequestResources> getRequestResources() {
        return requestResources;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.buyDevCard(view.getNickname(), getDevelopmentCard(), getSpace(), requestResources);
        view.sendMessage(new RequestResourcesAckMessage(success));
    }
}