package it.polimi.ingsw.server.model.cards.card;

import it.polimi.ingsw.server.model.boards.LorenzoBoard;

public abstract class LorenzoCard extends Card {
    /**
     * Activates the Lorenzo's token effect on the LorenzoBoard given by parameter
     * @param board LorenzoBoard where the effect needs to be activated
     */
    public abstract void activateLorenzo(LorenzoBoard board);
}
