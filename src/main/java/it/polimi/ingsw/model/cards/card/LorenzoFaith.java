package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public class LorenzoFaith implements LorenzoCard{

    private boolean refresh;
    private int amount;


    LorenzoFaith() {

    }


    @Override
    public LorenzoFaith makeClone() {
        return null;
    }

    @Override
    public void activateLorenzo(LorenzoBoard board) {

    }
}
