package it.polimi.ingsw.server.model.cards.ability;

import it.polimi.ingsw.server.model.boards.PlayerBoard;

public interface SpecialAbility {

    /**
     * Activates the leader's special ability on the board given by parameter
     * @param board Playerboard where the ability needs to be activated
     */
    void activateAbility(PlayerBoard board);
}
