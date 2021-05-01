package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.storage.Resource;
import it.polimi.ingsw.server.model.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class RequirementRes implements Requirement {

    private final Resource resource;


    public RequirementRes(Resource resource) {
        this.resource = resource;
    }


    /**
     * Gets the resource attribute
     * @return Returns resource value
     */
    public Resource getResource() {
        return resource;
    }

    @Override
    public boolean checkRequirement(PlayerBoard board){
        return Storage.checkContainedResources(board.createResourceStock(),new ArrayList<>(List.of(resource)));
    }
}
