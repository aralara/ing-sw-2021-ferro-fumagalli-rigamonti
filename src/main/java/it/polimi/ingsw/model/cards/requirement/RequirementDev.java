package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.card.CardColors;


public class RequirementDev implements Requirement {

    private CardColors color;
    private int level;
    private int number;


    public RequirementDev(){

    }


    @Override
    public boolean checkRequirement(PlayerBoard board){
        return board.checkRequirementDev(color, level, number);
    }
}
