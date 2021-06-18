package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;

import java.io.Serializable;

/**
 * Defines an interface to handle common parts of all abilities
 */
public interface SpecialAbility extends Serializable {

    /**
     * Activates the leader's special ability on the board given by parameter
     * @param board PlayerBoard where the ability needs to be activated
     */
    void activateAbility(PlayerBoard board);

    /**
     * Transforms an ability into a printable string
     * @return Returns a string that containing all of the ability information
     */
    String abilityToString();
}
