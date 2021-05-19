package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.CardColors;


public class RequirementDev implements Requirement {

    private final CardColors color;
    private final int level;
    private final int number;


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

    @Override
    public String requirementToString() {
        return number + " " + color + " level " + level + " development cards";
    }
}
