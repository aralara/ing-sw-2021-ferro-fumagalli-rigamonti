package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.utils.messages.ResourcesMessage;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ResourcesMarketMessage extends ResourcesMessage implements ServerActionMessage{

    public List<ResourceType> availableAbilities;

    public ResourcesMarketMessage(List<Resource> resources, List<ResourceType> availableAbilities) {
        super(resources);
        this.availableAbilities = availableAbilities;
    }


    @Override
    public void doAction(CLI client) {
        GraphicalCLI graphicalCLI = client.getGraphicalCLI();
        List<Resource> resources = getResources().stream()
                .filter(r -> r.getResourceType() != ResourceType.WILDCARD).collect(Collectors.toList());
        int     index,
                marblesLeft = (int) getResources().stream()
                        .filter(r -> r.getResourceType() == ResourceType.WILDCARD).count();

        while (marblesLeft > 0 && availableAbilities.size() > 0) {

            graphicalCLI.printString("You can choose a resource from the following types ( "
                    + marblesLeft + " wildcards left ):\n");

            availableAbilities.stream().collect(HashMap<Integer, String>::new,
                    (map, resourceType) -> map.put(map.size() + 1, resourceType.name()),
                    (map1, map2) -> { }).forEach((n, rt) -> graphicalCLI.printString(n + ")" + rt));

            do {
                graphicalCLI.printString("Please choose a valid resource type for the wildcard:");
                index = client.getNextInt() - 1;
            } while(0 > index || index > availableAbilities.size());

            resources.add(new Resource(availableAbilities.get(index), 1));

            availableAbilities.remove(index);
            marblesLeft--;
        }
        client.chooseShelvesManagement(resources);
    }
}
