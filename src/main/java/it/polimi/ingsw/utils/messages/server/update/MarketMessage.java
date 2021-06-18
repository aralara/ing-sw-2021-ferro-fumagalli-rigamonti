package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.server.model.market.*;

/**
 * TODO: fare javadoc
 */
public class MarketMessage implements ServerUpdateMessage {

    private final MarketView market;


    public MarketMessage(Market market) {
        this.market = new MarketView(market);
    }

    @Override
    public void doUpdate(ClientController client) {
        if(client.getMarket() == null)
            client.setMarket(market);//TODO: quick fix
        else
            client.getMarket().setMarket(market);
    }
}
