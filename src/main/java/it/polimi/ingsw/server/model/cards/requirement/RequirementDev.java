package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;
import it.polimi.ingsw.server.model.cards.card.CardColors;

/**
 * Handles methods relative to the development requirement
 */
public class RequirementDev implements Requirement {

    private final CardColors color;
    private final int level;
    private final int number;


    /**
     * Constructor for a RequirementDev object
     * @param color Color of the required development cards
     * @param level Level of the required development cards
     * @param number Number of the required development cards
     */
    public RequirementDev(CardColors color, int level, int number) {
        this.color = color;
        this.level = level;
        this.number = number;
    }


    @Override
    public boolean checkRequirement(PlayerBoard board) {
        return board.getDevelopmentBoard().checkRequirement(color, level, number);
    }

    @Override
    public String requirementToString() {
        return number + " " + color + " level " + level + " development cards";
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
}
