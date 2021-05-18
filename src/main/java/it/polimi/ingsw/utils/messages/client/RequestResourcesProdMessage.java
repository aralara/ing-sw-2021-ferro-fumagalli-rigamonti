package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.RequestResourcesAckMessage;

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
        view.sendMessage(new RequestResourcesAckMessage(success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {

    }

    @Override
    public void doNACKResponseAction(ClientController client) {

    }
}
