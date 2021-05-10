package it.polimi.ingsw.utils.messages.server;

import it.polimi.ingsw.client.cli.CLI;
import it.polimi.ingsw.server.model.cards.deck.DevelopmentDeck;

import java.util.List;

public class DevelopmentDecksMessage implements ServerActionMessage {

    private final List<DevelopmentDeck> developmentDecks;


    public DevelopmentDecksMessage(List<DevelopmentDeck> developmentDecks) {
        this.developmentDecks = developmentDecks;
    }


    public List<DevelopmentDeck> getDevelopmentDecks() {
        return developmentDecks;
    }

    @Override
    public void doAction(CLI client) {
        client.developmentDecksSetup(this);
    }
}
