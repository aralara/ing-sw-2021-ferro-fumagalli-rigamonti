package it.polimi.ingsw.model.cards.card;

import it.polimi.ingsw.model.boards.LorenzoBoard;

public class LorenzoDev implements LorenzoCard{

    private CardColors color;


    LorenzoDev(){

    }


    @Override
    public Card clone() {
        return null;
    }

    @Override
    public void activateLorenzo(LorenzoBoard board){

    }
}
