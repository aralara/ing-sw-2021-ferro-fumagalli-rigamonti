package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.client.cli.GraphicalCLI;
import it.polimi.ingsw.exceptions.NotExistingNickname;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.ResourceType;
import it.polimi.ingsw.utils.messages.ResourcesMessage;

import java.util.ArrayList;
import java.util.List;

public class ResourcesEqualizeMessage extends ResourcesMessage implements ServerActionMessage {

    public ResourcesEqualizeMessage(List<Resource> resources) {
        super(resources);
    }


    @Override
    public void doAction(CLI client) {
        GraphicalCLI graphicalCLI = client.getGraphicalCLI();
        List<Resource> newResources = new ArrayList<>();
        for(Resource resource : getResources()) {
            if (resource.getResourceType() == ResourceType.FAITH) {
                try {
                    client.playerBoardFromNickname(client.getNickname())
                            .getFaithBoard().setFaith(resource.getQuantity()); //TODO: Do per scontato che arriverà solo quello del player corretto?
                    graphicalCLI.printString(resource.getQuantity() + " " + resource.getResourceType()
                            + " has been added to your faith board\n");
                } catch (NotExistingNickname e) {
                    e.printStackTrace();
                }
            }
            else if (resource.getResourceType() == ResourceType.WILDCARD){
                newResources = client.resolveResourcesToEqualize(resource.getQuantity());
            }
        }
        if(newResources.size() > 0) {
            client.storeTempResources(newResources);
            graphicalCLI.printString("Now place the resources on the shelves:\n");
            client.chooseShelvesManagement(newResources);
        }
    }
}
