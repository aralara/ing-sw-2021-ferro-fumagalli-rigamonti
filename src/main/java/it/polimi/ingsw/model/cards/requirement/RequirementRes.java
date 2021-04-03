package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.Resource;
import it.polimi.ingsw.model.storage.Storage;

import java.util.ArrayList;
import java.util.List;

public class RequirementRes implements Requirement {

    Resource resource;


    public RequirementRes(){}


    @Override
    public boolean checkRequirement(PlayerBoard board){
        return Storage.checkContainedResources(board.createResourceStock(),new ArrayList<>(List.of(resource)));
    }
}
