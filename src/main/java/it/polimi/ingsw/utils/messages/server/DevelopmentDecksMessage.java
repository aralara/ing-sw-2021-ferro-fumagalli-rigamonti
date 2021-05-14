package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.ClientController;
import it.polimi.ingsw.client.structures.DevelopmentDeckView;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;

import java.util.List;

public class DevelopmentDecksMessage implements ServerUpdateMessage {

    private final List<DevelopmentDeck> developmentDecks;


    public DevelopmentDecksMessage(List<DevelopmentDeck> developmentDecks) {
        this.developmentDecks = developmentDecks;
    }


    public List<DevelopmentDeck> getDevelopmentDecks() {
        return developmentDecks;
    }

    @Override
    public void doUpdate(ClientController client) {
        List<DevelopmentDeckView> clientDevelopmentDecks = client.getDevelopmentDecks();
        for(DevelopmentDeck developmentDeck : developmentDecks) {
            clientDevelopmentDecks.add(new DevelopmentDeckView(developmentDeck.getDeck(),
                    developmentDeck.getDeckColor(), developmentDeck.getDeckLevel()));
        }
    }
}
