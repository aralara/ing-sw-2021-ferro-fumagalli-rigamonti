package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ActivateProductionsAckMessage;

import java.util.ArrayList;
import java.util.List;

public class ActivateProductionsMessageClient implements ClientActionMessage {

    private final List<Production> productions;


    public ActivateProductionsMessageClient(List<Production> productions) {
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
        productions.stream().map(Production::getConsumed).forEach(produced::addAll);
        return produced;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.canActivateProductions(view.getNickname(), getConsumed());
        view.sendMessage(new ActivateProductionsAckMessage(success));
    }
}
