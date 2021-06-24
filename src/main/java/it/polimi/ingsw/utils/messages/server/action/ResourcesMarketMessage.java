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


    /**
     * Constructor for a ResourcesMarketMessage given a list of resources and available abilities
     * @param resources Resources that were picked from the market
     * @param availableAbilities Available abilities to the player in order to pick WILDCARD marbles
     */
    public ResourcesMarketMessage(List<Resource> resources, List<ResourceType> availableAbilities) {    //TODO: availableAbilities da togliere?
        super(resources);
        this.availableAbilities = availableAbilities;
    }


    @Override
    public void doAction(ClientController client) {
        client.addMarketResources(getResources(), availableAbilities);
    }
}
