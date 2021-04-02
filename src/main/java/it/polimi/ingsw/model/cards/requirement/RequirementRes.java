package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.storage.Resource;

public class RequirementRes implements Requirement {

    Resource resource;


    public RequirementRes(){}


    @Override
    public boolean checkRequirement(PlayerBoard board){
        return false;
    }
}
