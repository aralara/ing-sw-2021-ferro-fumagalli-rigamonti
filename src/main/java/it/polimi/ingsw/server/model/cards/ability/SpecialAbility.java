package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;

import java.io.Serializable;

public interface SpecialAbility extends Serializable {

    /**
     * Activates the leader's special ability on the board given by parameter
     * @param board PlayerBoard where the ability needs to be activated
     */
    void activateAbility(PlayerBoard board);

    /**
     * Transform an ability into a string to be printed
     * @return A string that contains ability information
     */
    String abilityToString();
}
