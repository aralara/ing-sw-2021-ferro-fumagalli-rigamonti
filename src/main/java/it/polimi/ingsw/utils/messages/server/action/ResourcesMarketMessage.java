package it.polimi.ingsw.utils.messages.server.action;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.utils.messages.ResourcesMessage;

import java.util.List;
import java.util.stream.Collectors;

public class ResourcesMarketMessage extends ResourcesMessage implements ServerActionMessage {

    public List<ResourceType> availableAbilities;

    public ResourcesMarketMessage(List<Resource> resources, List<ResourceType> availableAbilities) {
        super(resources);
        this.availableAbilities = availableAbilities;
    }


    @Override
    public void doAction(ClientController client) {
        GraphicalCLI graphicalCLI = ((CLI) client).getGraphicalCLI();   //TODO: CAST A CLI ORRENDI, BRUTTI E ASSOLUTAMENTE TEMPORANEI IN TUTTO IL METODO
        List<Resource> resources = getResources().stream()
                .filter(r -> r.getResourceType() != ResourceType.WILDCARD).collect(Collectors.toList());
        int     index,
                marblesLeft = (int) getResources().stream()
                        .filter(r -> r.getResourceType() == ResourceType.WILDCARD).count();

        while (marblesLeft > 0 && availableAbilities.size() > 0) {

            graphicalCLI.printString("You can choose a resource from the following types ( "
                    + marblesLeft + " wildcards left ):\n");

            graphicalCLI.printNumberedList(availableAbilities, rt -> graphicalCLI.printString(rt.name()));

            do {
                graphicalCLI.printString("Please choose a valid resource type for the wildcard:");
                index = ((CLI) client).getGraphicalCLI().getNextInt() - 1;
            } while(0 > index || index > availableAbilities.size());

            resources.add(new Resource(availableAbilities.get(index), 1));

            availableAbilities.remove(index);
            marblesLeft--;
        }
        ((CLI) client).placeResourcesOnShelves(resources);
    }
}
