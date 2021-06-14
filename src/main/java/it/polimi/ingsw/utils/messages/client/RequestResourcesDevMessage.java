package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.cards.card.DevelopmentCard;
import it.polimi.ingsw.server.model.storage.RequestResources;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.List;

public class RequestResourcesDevMessage extends CanBuyDevelopmentCardMessage {

    private final List<RequestResources> requestResources;


    public RequestResourcesDevMessage(DevelopmentCard developmentCard, int space, List<RequestResources> requestResources) {
        super(developmentCard, space);
        this.requestResources = requestResources;
    }


    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().getController()
                .buyDevCard(view.getNickname(), getDevelopmentCard(), getSpace(), requestResources);
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Development card bought successfully", false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.ackNotification("Unable to buy development card with the selected resources", false);
        client.chooseDevelopmentStorages(getDevelopmentCard(), getSpace(), getDevelopmentCard().getCost());
    }
}
