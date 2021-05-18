package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ShelvesConfigurationAckMessage;

import java.util.List;

public class ShelvesConfigurationMessage extends ClientActionMessage {

    private final List<Shelf> shelves;
    private final List<Resource> extra;


    public ShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> extra) {
        this.shelves = shelves;
        this.extra = extra;
    }


    public List<Shelf> getShelves() {
        return shelves;
    }

    public List<Resource> getExtra() {
        return extra;
    }

    @Override
    public void doAction(VirtualView view, Controller controller) {
        boolean success = controller.addResourcesToWarehouse(view.getNickname(), shelves, extra);
        view.sendMessage(new ShelvesConfigurationAckMessage(success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {

    }

    @Override
    public void doNACKResponseAction(ClientController client) {

    }
}
