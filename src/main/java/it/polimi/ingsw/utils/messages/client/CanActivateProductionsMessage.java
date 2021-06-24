package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Client message that activates productions
 */
public class CanActivateProductionsMessage extends ClientActionMessage {

    private final List<Production> productions;


    /**
     * Constructor for the message from a list of productions
     * @param productions List of productions to activate
     */
    public CanActivateProductionsMessage(List<Production> productions) {
        this.productions = productions;
    }


    /**
     * Gets the productions attribute
     * @return Returns the value of productions
     */
    public List<Production> getProductions() {
        return productions;
    }

    /**
     * Gets all the consumed resources by all the productions
     * @return Return a list containing the resources
     */
    public List<Resource> getConsumed() {
        List<Resource> consumed = new ArrayList<>();
        productions.stream().map(Production::getConsumed).forEach(l -> l.forEach(r -> consumed.add(r.makeClone())));
        return consumed;
    }

    /**
     * Gets all the consumed produced by all the productions
     * @return Return a list containing the resources
     */
    public List<Resource> getProduced() {
        List<Resource> produced = new ArrayList<>();
        productions.stream().map(Production::getProduced).forEach(l -> l.forEach(r -> produced.add(r.makeClone())));
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
        client.setMainActionPlayed(true);
        client.chooseProductionStorages(productions, getConsumed());
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        client.setMainActionPlayed(false);
        client.selectProductions();
    }
}
