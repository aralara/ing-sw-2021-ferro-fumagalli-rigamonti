package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerActionAckMessage;

import java.util.ArrayList;
import java.util.List;

public class RequestResourcesProdMessage extends CanActivateProductionsMessage {

    private final List<RequestResources> requestResources;


    public RequestResourcesProdMessage(List<Production> productions, List<RequestResources> requestResources) {
        super(productions);
        this.requestResources = requestResources;
    }


    public List<RequestResources> getRequestResources() {
        return requestResources;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.activateProductions(view.getNickname(), getProduced(), requestResources);
        view.sendMessage(new ServerActionAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        //TODO: deve fare qualcosa qui?
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        List<Resource> resources = new ArrayList<>();
        getProductions().forEach(p -> resources.addAll(p.getConsumed()));
        List<RequestResources> requestResources = client.chooseStorages(resources);
        client.getMessageHandler().sendActionMessage(
                new RequestResourcesProdMessage(getProductions(), requestResources));
    }
}
