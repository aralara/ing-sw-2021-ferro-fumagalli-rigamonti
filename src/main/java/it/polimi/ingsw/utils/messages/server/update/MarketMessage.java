package it.polimi.ingsw.utils.messages.server.update;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.MarketView;
import it.polimi.ingsw.server.model.market.*;

/**
 * Server update message for the game's market
 */
public class MarketMessage implements ServerUpdateMessage {

    private final MarketView market;


    /**
     * Constructor for a MarketMessage given a market
     * @param market Market contained in the MarketMessage
     */
    public MarketMessage(Market market) {
        this.market = new MarketView(market);
    }


    @Override
    public void doUpdate(ClientController client) {
        if(client.getMarket() == null)
            client.setMarket(market);
        else
            client.getMarket().setMarket(market);
    }
}
