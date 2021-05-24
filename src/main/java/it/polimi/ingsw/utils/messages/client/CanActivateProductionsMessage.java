package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.ArrayList;
import java.util.List;

public class CanActivateProductionsMessage extends ClientActionMessage {

    private final List<Production> productions;


    public CanActivateProductionsMessage(List<Production> productions) {
        this.productions = productions;
    }


    public List<Production> getProductions() {
        return productions;
    }

    public List<Resource> getConsumed() {
        List<Resource> consumed = new ArrayList<>();
        productions.stream().map(Production::getConsumed).forEach(consumed::addAll);
        return consumed;
    }

    public List<Resource> getProduced() {
        List<Resource> produced = new ArrayList<>();
        productions.stream().map(Production::getProduced).forEach(produced::addAll);
        return produced;
    }

    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().getController()
                .canActivateProductions(view.getNickname(), getConsumed());
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        List<Resource> resources = new ArrayList<>();
        productions.forEach(p -> resources.addAll(p.getConsumed()));
        List<RequestResources> requestResources = client.chooseStorages(resources);
        client.getMessageHandler().sendClientMessage(new RequestResourcesProdMessage(productions, requestResources));
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.setMainActionPlayed(false);
        client.selectProductions();
    }
}
