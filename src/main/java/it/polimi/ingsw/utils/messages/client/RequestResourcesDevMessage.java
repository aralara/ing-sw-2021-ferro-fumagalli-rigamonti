package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerActionAckMessage;

import java.util.List;

public class RequestResourcesDevMessage extends CanBuyDevelopmentCardMessage {

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
        view.sendMessage(new ServerActionAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: deve fare qualcosa qui?
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        List<RequestResources> requestResources = client.chooseStorages(getDevelopmentCard().getCost());
        client.getMessageHandler().sendActionMessage(
                new RequestResourcesDevMessage(getDevelopmentCard(), getSpace(), requestResources));
    }
}
