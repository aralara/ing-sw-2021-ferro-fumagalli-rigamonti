package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.ArrayList;
import java.util.List;

public class ShelvesConfigurationMessage extends ClientActionMessage {

    private final List<Shelf> shelves;
    private final List<Resource> placed;
    private final List<Resource> extra;


    public ShelvesConfigurationMessage(List<Shelf> shelves) {
        this.shelves = shelves;
        this.placed = new ArrayList<>();
        this.extra = new ArrayList<>();
    }

    public ShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> placed, List<Resource> extra) {
        this.shelves = shelves;
        this.placed = placed;
        this.extra = extra;
    }


    @Override
    public void doAction(VirtualView view) {
        boolean success = view.getGameHandler().getController()
                .addResourcesToWarehouse(view.getNickname(), shelves, extra);
        view.sendMessage(new ServerAckMessage(getUuid(), success));
    }

    @Override
    public void doACKResponseAction(ClientController client) {
        client.ackNotification("Shelves set successfully", false);
    }

    @Override
    public void doNACKResponseAction(ClientController client) {
        if(placed.size() > 0)   // true if the warehouse has been rearranged while adding resources to it
            client.placeResourcesOnShelves(placed);
        else
            client.ackNotification("Invalid shelf configuration", false);
    }
}
