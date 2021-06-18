package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;

import java.util.List;

/**
 * Server message that gives to the player the resources he selected from the market
 */
public class ResourcesMarketMessage extends ResourcesMessage implements ServerActionMessage {

    public List<ResourceType> availableAbilities;

    public ResourcesMarketMessage(List<Resource> resources, List<ResourceType> availableAbilities) {
        super(resources);
        this.availableAbilities = availableAbilities;
    }


    @Override
    public void doAction(ClientController client) {
        client.addMarketResources(getResources(), availableAbilities);
    }
}
