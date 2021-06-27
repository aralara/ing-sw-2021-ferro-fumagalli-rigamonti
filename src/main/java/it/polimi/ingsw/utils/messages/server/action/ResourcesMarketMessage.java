package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.Resource;

import java.util.List;

/**
 * Server message that gives to the player the resources he selected from the market
 */
public class ResourcesMarketMessage extends ResourcesMessage implements ServerActionMessage {

    /**
     * Constructor for a ResourcesMarketMessage given a list of resources
     * @param resources Resources that were picked from the market
     */
    public ResourcesMarketMessage(List<Resource> resources) {
        super(resources);
    }


    @Override
    public void doAction(ClientController client) {
        client.addMarketResources(getResources());
    }
}
