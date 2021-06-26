package it.polimi.ingsw.utils.messages.client;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.*;
import it.polimi.ingsw.server.view.VirtualView;
import it.polimi.ingsw.utils.messages.server.ack.ServerAckMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Client message that rearranges the shelves' configuration for the player's warehouse
 */
public class ShelvesConfigurationMessage extends ClientActionMessage {

    private final List<Shelf> shelves;
    private final List<Resource> placed;
    private final List<Resource> extra;


    /**
     * Constructor for a ShelvesConfigurationMessage given a list of shelves
     * @param shelves Shelves that will reconfigure the warehouse
     */
    public ShelvesConfigurationMessage(List<Shelf> shelves) {
        this.shelves = shelves;
        this.placed = new ArrayList<>();
        this.extra = new ArrayList<>();
    }

    /**
     * Constructor for a ShelvesConfigurationMessage given a list of shelves and various information in case some
     * resources need to be added
     * @param shelves Shelves that will reconfigure the warehouse
     * @param placed Added resources
     * @param extra Extra resources containing faith and discarded resources
     */
    public ShelvesConfigurationMessage(List<Shelf> shelves, List<Resource> placed, List<Resource> extra) {
        this.shelves = shelves;
        this.placed = placed;
        this.extra = extra;
    }


    @Override
    public void doAction(VirtualView view) {    //TODO: sicurezza?
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
            client.ackNotification("Invalid shelf configuration", true);
    }
}
