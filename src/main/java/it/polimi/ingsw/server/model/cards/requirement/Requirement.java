package it.polimi.ingsw.server.model.cards.requirement;

import it.polimi.ingsw.server.model.boards.PlayerBoard;

import java.io.Serializable;

/**
 * Defines an interface to handle common parts of all requirements
 */
public interface Requirement extends Serializable {

    /**
     * Checks if a specific requirement is met on the PlayerBoard given by parameter
     * @param board PlayerBoard where the requirement needs to be checked
     * @return Returns true if the requirement is met, false otherwise
     */
    boolean checkRequirement(PlayerBoard board);

    /**
     * Transforms a requirement into a printable string
     * @return Returns a string that contains all of the requirement information
     */
    String requirementToString();
}
