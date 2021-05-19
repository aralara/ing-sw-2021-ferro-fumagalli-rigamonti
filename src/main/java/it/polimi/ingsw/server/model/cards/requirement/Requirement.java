package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;

import java.io.Serializable;

public interface Requirement extends Serializable {

    /**
     * Checks if a specific requirement is met on the PlayerBoard given by parameter
     * @param board PlayerBoard where the requirement needs to be checked
     * @return Returns true if the requirement is met, false otherwise
     */
    boolean checkRequirement(PlayerBoard board);

    /**
     * Transform a requirement into a string to be printed
     * @return A string that contains requirement information
     */
    String requirementToString();
}
