package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public interface LorenzoCard extends Card {

    /**
     * Activates the Lorenzo's token effect on the LorenzoBoard given by parameter
     * @param board LorenzoBoard where the effect needs to be activated
     */
    void activateLorenzo(LorenzoBoard board);
}
