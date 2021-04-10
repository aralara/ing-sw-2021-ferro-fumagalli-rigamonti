package it.polimi.ingsw.model.cards.requirement;

import it.polimi.ingsw.model.boards.PlayerBoard;
import it.polimi.ingsw.model.cards.card.Card;

public interface Requirement {

    /**
     * Checks if a specific requirement is met on the PlayerBoard given by parameter
     * @param board PlayerBoard where the requirement needs to be checked
     * @return Returns true if the requirement is met, false otherwise
     */
    boolean checkRequirement(PlayerBoard board);
}
