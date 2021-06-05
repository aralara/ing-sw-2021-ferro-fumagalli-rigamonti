package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.List;

public class RequestResourcesProdMessage extends CanActivateProductionsMessage {

    private final List<RequestResources> requestResources;


    public RequestResourcesProdMessage(List<Production> productions, List<RequestResources> requestResources) {
        super(productions);
        this.requestResources = requestResources;
    }


    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().getController()
                .activateProductions(view.getNickname(), getProductions(), requestResources);
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: gestione ACK
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.chooseProductionStorages(getProductions(), getConsumed());
    }
}
