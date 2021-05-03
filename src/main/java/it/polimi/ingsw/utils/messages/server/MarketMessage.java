package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.server.model.market.*;
import it.polimi.ingsw.utils.messages.Message;

public class MarketMessage implements Message {

    private Marble[][] marbleMatrix;
    private Marble floatingMarble;


    public MarketMessage(Market market) {
        this.marbleMatrix = market.getMarbleMatrix();
        this.floatingMarble = market.getFloatingMarble();
    }


    public Marble[][] getMarbleMatrix() {
        return marbleMatrix;
    }

    public Marble getFloatingMarble() {
        return floatingMarble;
    }
}
