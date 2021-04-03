package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.DevelopmentBoard;
import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.card.CardColors;
import it.polimi.ingsw.model.cards.card.DevelopmentCard;

import java.util.ArrayList;
import java.util.List;

public class RequirementDev implements Requirement {

    private CardColors color;
    private int level;
    private int number;


    public RequirementDev(){

    }


    @Override
    public boolean checkRequirement(PlayerBoard board){
        //%% DevelopmentBoard temp = board.getDevelopmentBoard();
        DevelopmentBoard tempDevBoard = new DevelopmentBoard();
        List<DevelopmentCard> tempCards = new ArrayList<>();
        int tempCount=0;

        for (DevelopmentCard tempCard : tempCards) {
            if ((tempCard.getColor() == color) && (tempCard.getLevel()) == level) {
                tempCount++;
            }
        }
        return tempCount >= number;
    }
}
