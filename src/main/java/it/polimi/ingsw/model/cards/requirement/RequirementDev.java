package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.card.CardColors;


public class RequirementDev implements Requirement {

    private CardColors color;
    private int level;
    private int number;


    public RequirementDev(CardColors color, int level, int number) {
        this.color = color;
        this.level = level;
        this.number = number;
    }


    /**
     * Gets the color attribute
     * @return Returns color value
     */
    public CardColors getColor() {
        return color;
    }

    /**
     * Gets the level attribute
     * @return Returns level value
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the number attribute
     * @return Returns number value
     */
    public int getNumber() {
        return number;
    }

    @Override
    public boolean checkRequirement(PlayerBoard board) {
        return board.getDevelopmentBoard().checkRequirement(color, level, number);
    }
}
