package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.model.market.*;

public class MarketMessage implements ServerActionMessage {

    private final Marble[][] marbleMatrix;
    private final Marble floatingMarble;


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

    @Override
    public void doAction(CLI client) {
        client.updateMarket(this);
    }
}
